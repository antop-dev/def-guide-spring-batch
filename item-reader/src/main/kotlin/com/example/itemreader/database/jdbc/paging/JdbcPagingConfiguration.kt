package com.example.itemreader.database.jdbc.paging

import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.Order
import org.springframework.batch.item.database.PagingQueryProvider
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder
import org.springframework.batch.item.database.support.H2PagingQueryProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class JdbcPagingConfiguration(
    val stepBuilderFactory: StepBuilderFactory,
    val jobBuilderFactory: JobBuilderFactory,
) {

    @Bean
    @StepScope // jobParameter를 사용하려면 @StepScope를 사용해야 한다.
    fun customerItemReader(
        dataSource: DataSource,
        queryProvider: PagingQueryProvider,
        @Value("#{jobParameters['city']}") city: String?
    ): ItemReader<Customer> {
        return JdbcPagingItemReaderBuilder<Customer>()
            .name("customerItemReader")
            .dataSource(dataSource)
            .queryProvider(queryProvider)
            .parameterValues(mapOf("city" to city))
            .rowMapper(CustomerRowMapper())
            .build()
    }

    @Bean
    fun queryProvider(dataSource: DataSource): PagingQueryProvider {
        return H2PagingQueryProvider().apply {
            setSelectClause("select *")
            setFromClause("from customer")
            setWhereClause("where city = :city")
            sortKeys = mapOf("id" to Order.ASCENDING)
        }
    }

    @Bean
    fun itemWriter(): ItemWriter<Customer> = ItemWriter { items ->
        items.forEach { println(it) }
    }

    @Bean
    fun copyFileStep(dataSource: DataSource, queryProvider: PagingQueryProvider) =
        stepBuilderFactory.get("copyFileStep")
            .chunk<Customer, Customer>(10)
            .reader(customerItemReader(dataSource, queryProvider, null))
            .writer(itemWriter())
            .build()

    @Bean
    fun copyFileJob(dataSource: DataSource, queryProvider: PagingQueryProvider) = jobBuilderFactory.get("copyFileJob")
        .start(copyFileStep(dataSource, queryProvider))
        .build();

}
