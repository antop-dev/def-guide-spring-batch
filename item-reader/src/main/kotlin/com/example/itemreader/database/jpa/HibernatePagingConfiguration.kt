package com.example.itemreader.database.jpa

import org.hibernate.SessionFactory
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.builder.HibernatePagingItemReaderBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.persistence.EntityManagerFactory

@Configuration
class HibernatePagingConfiguration(
    val stepBuilderFactory: StepBuilderFactory,
    val jobBuilderFactory: JobBuilderFactory,
) {

    @Bean
    @StepScope
    fun customerItemReader(
        entityManagerFactory: EntityManagerFactory,
        @Value("#{jobParameters['city']}") city: String?
    ) = HibernatePagingItemReaderBuilder<Customer>()
        .name("customerItemReader")
        .sessionFactory(entityManagerFactory.unwrap(SessionFactory::class.java))
        .queryString("from Customer where city = :city")
        .parameterValues(mapOf("city" to city))
        // 페이징 크기는 청크 크기와 다르다!
        // 페이징이 5고 청크가 3일 때 데이터 양이 15개면
        // 쿼리는 3번 수행되고 청크는 5번 나눠진다.
        .pageSize(5)
        .build()

    @Bean
    fun itemWriter(): ItemWriter<Customer> = ItemWriter { items ->
        items.forEach { println(it) }
    }

    @Bean
    fun copyFileStep(entityManagerFactory: EntityManagerFactory) =
        stepBuilderFactory.get("copyFileStep")
            .chunk<Customer, Customer>(3)
            .reader(customerItemReader(entityManagerFactory, null))
            .writer(itemWriter())
            .build()

    @Bean
    fun copyFileJob(entityManagerFactory: EntityManagerFactory) = jobBuilderFactory.get("copyFileJob")
        .start(copyFileStep(entityManagerFactory))
        .build()

}
