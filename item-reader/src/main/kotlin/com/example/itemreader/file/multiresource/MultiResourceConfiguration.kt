package com.example.itemreader.file.multiresource

import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.batch.item.file.builder.MultiResourceItemReaderBuilder
import org.springframework.batch.item.file.mapping.PatternMatchingCompositeLineMapper
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource

@Configuration
class MultiResourceConfiguration(
    val stepBuilderFactory: StepBuilderFactory,
    val jobBuilderFactory: JobBuilderFactory
) {

    @Bean
    @StepScope
    fun multiCustomerReader(@Value("#{jobParameters['customerFile']}") inputFile: Array<Resource>?) =
        MultiResourceItemReaderBuilder<Any>()
            .name("multiCustomerReader")
            .resources(*inputFile!!)
            .delegate(customerFileReader())
            .build()

    @Bean
    fun customerFileReader() = CustomFileReader(customerItemReader())

    @Bean
    fun customerItemReader() =
        FlatFileItemReaderBuilder<Any>()
            .name("customerItemReader")
            .lineMapper(lineTokenizer())
            .build()

    @Bean
    fun lineTokenizer(): PatternMatchingCompositeLineMapper<Any> {
        val lineTokenizers = mapOf(
            "CUST*" to customerLineTokenizer(),
            "TRANS*" to transactionLineTokenizer()
        )
        val fieldSetMappers = mapOf(
            "CUST*" to CustomerFieldSetMapper(),
            "TRANS*" to TransactionFieldSetMapper()
        )
        return PatternMatchingCompositeLineMapper<Any>().apply {
            setTokenizers(lineTokenizers)
            setFieldSetMappers(fieldSetMappers)
        }
    }

    @Bean
    fun transactionLineTokenizer() = DelimitedLineTokenizer().apply {
        setNames("accountNumber", "transactionDate", "amount")
        setIncludedFields(1, 2, 3)
    }

    @Bean
    fun customerLineTokenizer() = DelimitedLineTokenizer().apply {
        setNames("firstName", "middleInitial", "lastName", "address", "city", "state", "zipCode")
        setIncludedFields(1, 2, 3, 4, 5, 6, 7)
    }

    @Bean
    fun itemWriter(): ItemWriter<Any> = ItemWriter { items ->
        items.forEach { println(it) }
    }

    @Bean
    fun copyFileStep() = stepBuilderFactory.get("copyFileStep")
        .chunk<Any, Any>(10)
        .reader(multiCustomerReader(null))
        .writer(itemWriter())
        .build()

    @Bean
    fun copyFileJob() = jobBuilderFactory.get("copyFileJob")
        .start(copyFileStep())
        .build()

}