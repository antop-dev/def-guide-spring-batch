package org.antop.chapter6.transaction

import org.antop.chapter6.transaction.batch.TransactionApplierProcessor
import org.antop.chapter6.transaction.batch.TransactionReader
import org.antop.chapter6.transaction.domain.AccountSummary
import org.antop.chapter6.transaction.domain.Transaction
import org.antop.chapter6.transaction.domain.TransactionDaoSupport
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder
import org.springframework.batch.item.file.FlatFileItemWriter
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder
import org.springframework.batch.item.file.mapping.PassThroughFieldSetMapper
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor
import org.springframework.batch.item.file.transform.DelimitedLineAggregator
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer
import org.springframework.batch.item.file.transform.FieldSet
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.io.Resource
import javax.sql.DataSource

@EnableBatchProcessing
@SpringBootApplication
class TransactionProcessingJob(
    val jobBuilderFactory: JobBuilderFactory,
    val stepBuilderFactory: StepBuilderFactory,
    val dataSource: DataSource
) {
    /*
     * 첫번째 스탭 : CSV 파일의 데이터를 데이터베이스로 옮김
     */
    @Bean
    @StepScope
    fun transactionReader() = TransactionReader(fileItemReader(null))

    @Bean
    @StepScope
    fun fileItemReader(@Value("#{jobParameters['transactionFile']}") inputFile: Resource?) =
        FlatFileItemReaderBuilder<FieldSet>()
            .name("fileItemReader")
            .resource(inputFile!!)
            .lineTokenizer(DelimitedLineTokenizer())
            .fieldSetMapper(PassThroughFieldSetMapper())
            .build()

    @Bean
    fun transactionWriter() = JdbcBatchItemWriterBuilder<Transaction>()
        .itemSqlParameterSourceProvider(BeanPropertyItemSqlParameterSourceProvider())
        .dataSource(dataSource)
        .sql(
            "INSERT INTO TRANSACTION " +
                    "(ACCOUNT_SUMMARY_ID, TIMESTAMP, AMOUNT) " +
                    "VALUES ((SELECT ID FROM ACCOUNT_SUMMARY " +
                    "	WHERE ACCOUNT_NUMBER = :accountNumber), " +
                    ":timestamp, :amount)"
        )
        .build()

    @Bean
    fun importTransactionFileStep() = stepBuilderFactory.get("importTransactionFileStep")
        .chunk<Transaction, Transaction>(100)
        .reader(transactionReader())
        .writer(transactionWriter())
        .allowStartIfComplete(true)
        .listener(transactionReader())
        .build()

    /*
     * 두번째 스탭 : 거래 정보를 계좌에 적용
     */
    @Bean
    @StepScope
    fun accountSummaryReader() = JdbcCursorItemReaderBuilder<AccountSummary>()
        .name("accountSummaryReader")
        .dataSource(dataSource)
        // 트랜잭션 데이터에 있는 계좌 정보만 읽기
        .sql(
            "SELECT ACCOUNT_NUMBER, CURRENT_BALANCE " +
                    "FROM ACCOUNT_SUMMARY A " +
                    "WHERE A.ID IN (" +
                    "	SELECT DISTINCT T.ACCOUNT_SUMMARY_ID " +
                    "	FROM TRANSACTION T) " +
                    "ORDER BY A.ACCOUNT_NUMBER"
        )
        .rowMapper { rs, _ ->
            AccountSummary(
                accountNumber = rs.getString("account_number"),
                currentBalance = rs.getDouble("current_balance")
            )
        }
        .build()

    @Bean
    fun transactionDao() = TransactionDaoSupport(dataSource)

    @Bean
    fun transactionApplierProcessor() = TransactionApplierProcessor(transactionDao())

    @Bean
    fun accountSummaryWriter() = JdbcBatchItemWriterBuilder<AccountSummary>()
        .dataSource(dataSource)
        .itemSqlParameterSourceProvider(BeanPropertyItemSqlParameterSourceProvider())
        .sql(
            "UPDATE ACCOUNT_SUMMARY " +
                    "SET CURRENT_BALANCE = :currentBalance " +
                    "WHERE ACCOUNT_NUMBER = :accountNumber"
        )
        .build()

    @Bean
    fun applyTransactionsStep() = stepBuilderFactory.get("ApplyTransactionsStep")
        .chunk<AccountSummary, AccountSummary>(100)
        .reader(accountSummaryReader())
        .processor(transactionApplierProcessor())
        .writer(accountSummaryWriter())
        .build()

    /*
     * 세번째 스탭 : 계좌 내역을 CSV 파일로 출력한다.
     */
    @Bean
    @StepScope
    fun accountSummaryFileWriter(@Value("#{jobParameters['summaryFile']}") summaryFile: Resource?): FlatFileItemWriter<AccountSummary> {
        val lineAggregator = DelimitedLineAggregator<AccountSummary>().apply {
            val fieldExtractor = BeanWrapperFieldExtractor<AccountSummary>().apply {
                setNames(arrayOf("accountNumber", "currentBalance"))
                afterPropertiesSet()
            }
            setFieldExtractor(fieldExtractor)
        }
        return FlatFileItemWriterBuilder<AccountSummary>()
            .name("accountSummaryFileWriter")
            .resource(summaryFile!!)
            .lineAggregator(lineAggregator)
            .build()
    }

    @Bean
    fun generateAccountSummaryStep() = stepBuilderFactory.get("generateAccountSummaryStep")
        .chunk<AccountSummary, AccountSummary>(100)
        .reader(accountSummaryReader())
        .writer(accountSummaryFileWriter(null))
        .build()

    /*
     * 잡
     */
    @Bean
    fun job() = jobBuilderFactory.get("transactionJob")
        .start(importTransactionFileStep())
        .next(applyTransactionsStep())
        .next(generateAccountSummaryStep())
        .build()

}

fun main() {
    val args = arrayOf(
        "transactionFile=classpath:input/transactionFile.csv",
        // 출력 위치 변경
        "summaryFile=file:///Users/antop/summaryFile.csv"
    )
    runApplication<TransactionProcessingJob>(*args)
}
