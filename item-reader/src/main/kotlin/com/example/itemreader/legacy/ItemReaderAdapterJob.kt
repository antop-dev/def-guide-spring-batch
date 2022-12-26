package com.example.itemreader.legacy

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.adapter.ItemReaderAdapter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@EnableBatchProcessing
@Configuration
class ItemReaderAdapterJob(
    val stepBuilderFactory: StepBuilderFactory,
    val jobBuilderFactory: JobBuilderFactory,
) {
    @Bean
    fun customerService() = CustomerService()

    @Bean
    @StepScope
    fun customerItemReader(customerService: CustomerService) = ItemReaderAdapter<Customer>().apply {
        setTargetObject(customerService)
        setTargetMethod("getCustomer")
    }

    @Bean
    fun itemWriter(): ItemWriter<Customer> = ItemWriter { items ->
        items.forEach { println(it) }
    }

    @Bean
    fun copyFileStep(customerService: CustomerService) =
        stepBuilderFactory.get("copyFileStep")
            .chunk<Customer, Customer>(7)
            .reader(customerItemReader(customerService))
            .writer(itemWriter())
            .build()

    @Bean
    fun copyFileJob(customerService: CustomerService) = jobBuilderFactory.get("copyFileJob")
        .start(copyFileStep(customerService))
        .build()

}
