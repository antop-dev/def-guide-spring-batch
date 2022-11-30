package com.example.itemreader.file.delimited

import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource

@Configuration
class DelimitedConfiguration(
    val stepBuilderFactory: StepBuilderFactory,
    val jobBuilderFactory: JobBuilderFactory
) {

    @Bean
    @StepScope
    fun customerItemReader(@Value("#{jobParameters['customerFile']}") inputFile: Resource?) =
        FlatFileItemReaderBuilder<Customer>()
            .name("customerItemReader")
            .resource(inputFile!!)
            // 한 라인을 알아서(",") 파싱하게 하고 FieldSet → Customer 변환 시 커스터마이징
//            .delimited() // 구분자
//            .names("firstName", "middleInitial", "lastName", "addressNumber", "street", "city", "state", "zipCode")
//            .fieldSetMapper(CustomerFieldSetMapper())
            // 한 라인을 파싱할 때 커스터마이징하고 Customer 매핑은 기본 매핑
            .lineTokenizer(com.example.itemreader.file.delimited.CustomerFileLineTokenizer())
            .fieldSetMapper {
                Customer(
                    it.readString("firstName"),
                    it.readString("middleInitial"),
                    it.readString("lastName"),
                    it.readString("address"),
                    it.readString("city"),
                    it.readString("state"),
                    it.readString("zipCode")
                )
            }
            .build()

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
        .build()

}
