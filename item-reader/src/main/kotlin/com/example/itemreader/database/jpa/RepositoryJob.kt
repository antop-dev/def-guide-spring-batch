package com.example.itemreader.database.jpa

import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Sort

@Configuration
class RepositoryJob(
    val stepBuilderFactory: StepBuilderFactory,
    val jobBuilderFactory: JobBuilderFactory,
) {

    @Bean
    @StepScope
    fun customerItemReader(
        repository: CustomerRepository,
        @Value("#{jobParameters['city']}") city: String?
    ) = RepositoryItemReaderBuilder<Customer>()
        .name("customerItemReader")
        .arguments(city)
        .methodName("findByCity")
        .repository(repository)
        .sorts(mapOf("lastName" to Sort.Direction.ASC))
        .build()

    @Bean
    fun itemWriter(): ItemWriter<Customer> = ItemWriter { items ->
        items.forEach { println(it) }
    }

    @Bean
    fun copyFileStep(repository: CustomerRepository) =
        stepBuilderFactory.get("copyFileStep")
            .chunk<Customer, Customer>(3)
            .reader(customerItemReader(repository, null))
            .writer(itemWriter())
            .build()

    @Bean
    fun copyFileJob(repository: CustomerRepository) = jobBuilderFactory.get("copyFileJob")
        .start(copyFileStep(repository))
        .build()

}
