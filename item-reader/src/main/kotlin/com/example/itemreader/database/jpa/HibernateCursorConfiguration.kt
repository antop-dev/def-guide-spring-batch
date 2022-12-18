package com.example.itemreader.database.jpa

import org.hibernate.SessionFactory
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.builder.HibernateCursorItemReaderBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.persistence.EntityManagerFactory

@Configuration
class HibernateCursorConfiguration(
    val stepBuilderFactory: StepBuilderFactory,
    val jobBuilderFactory: JobBuilderFactory,
) {

    @Bean
    @StepScope
    fun customerItemReader(
        entityManagerFactory: EntityManagerFactory,
        @Value("#{jobParameters['city']}") city: String?
    ) = HibernateCursorItemReaderBuilder<Customer>()
        .name("customerItemReader")
        .sessionFactory(entityManagerFactory.unwrap(SessionFactory::class.java))
        .queryString("from Customer where city = :city")
        .parameterValues(mapOf("city" to city))
        .build()

    @Bean
    fun itemWriter(): ItemWriter<Customer> = ItemWriter { items ->
        items.forEach { println(it) }
    }

    @Bean
    fun copyFileStep(entityManagerFactory: EntityManagerFactory) =
        stepBuilderFactory.get("copyFileStep")
            .chunk<Customer, Customer>(10)
            .reader(customerItemReader(entityManagerFactory, null))
            .writer(itemWriter())
            .build()

    @Bean
    fun copyFileJob(entityManagerFactory: EntityManagerFactory) = jobBuilderFactory.get("copyFileJob")
        .start(copyFileStep(entityManagerFactory))
        .build()

}
