package com.example.itemreader.database.jdbc

import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class JdbcCursorConfiguration(
    val stepBuilderFactory: StepBuilderFactory,
    val jobBuilderFactory: JobBuilderFactory,
) {

    @Bean
    fun customerItemReader(dataSource: DataSource): ItemReader<Customer> {
        return JdbcCursorItemReaderBuilder<Customer>()
            .name("customerItemReader")
            .dataSource(dataSource)
            .sql("select * from customer")
            .rowMapper(CustomerRowMapper())
            .build()
    }

    @Bean
    fun itemWriter(): ItemWriter<Customer> = ItemWriter { items ->
        items.forEach { println(it) }
    }

    @Bean
    fun copyFileStep(dataSource: DataSource) = stepBuilderFactory.get("copyFileStep")
        .chunk<Customer, Customer>(10)
        .reader(customerItemReader(dataSource))
        .writer(itemWriter())
        .build()

    @Bean
    fun copyFileJob(dataSource: DataSource) = jobBuilderFactory.get("copyFileJob")
        .start(copyFileStep(dataSource))
        .build();

}
