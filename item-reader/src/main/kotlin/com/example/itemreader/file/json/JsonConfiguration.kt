package com.example.itemreader.file.json

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.json.JacksonJsonObjectReader
import org.springframework.batch.item.json.JsonItemReader
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import java.text.SimpleDateFormat

@Configuration
class JsonConfiguration(
    val stepBuilderFactory: StepBuilderFactory,
    val jobBuilderFactory: JobBuilderFactory
) {

    @Bean
    @StepScope
    fun customerItemReader(@Value("#{jobParameters['customerFile']}") inputFile: Resource?): JsonItemReader<Customer> {
        val om = ObjectMapper().apply {
            dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
            registerModule(KotlinModule.Builder().build())
            registerModule(JavaTimeModule())
        }
        val jsonObjectReader = JacksonJsonObjectReader(om, Customer::class.java)
        return JsonItemReaderBuilder<Customer>()
            .name("customerFileReader")
            .jsonObjectReader(jsonObjectReader)
            .resource(inputFile!!)
            .build()
    }

    @Bean
    fun itemWriter(): ItemWriter<Customer> = ItemWriter { items ->
        items.forEach { println(it) }
    }

    @Bean
    fun copyFileStep() = stepBuilderFactory.get("copyFileStep")
        .chunk<Customer, Customer>(10)
        .reader(customerItemReader(null))
        .writer(itemWriter())
        .build()

    @Bean
    fun copyFileJob() = jobBuilderFactory.get("copyFileJob")
        .start(copyFileStep())
        .build();

}
