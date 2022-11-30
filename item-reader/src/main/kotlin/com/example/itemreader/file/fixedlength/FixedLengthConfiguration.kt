package com.example.itemreader.file.fixedlength

import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.batch.item.file.transform.Range
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource

@Configuration
class FixedLengthConfiguration(
    val stepBuilderFactory: StepBuilderFactory,
    val jobBuilderFactory: JobBuilderFactory
) {

    @Bean
    @StepScope
    fun customerItemReader(@Value("#{jobParameters['customerFile']}") inputFile: Resource?) =
        FlatFileItemReaderBuilder<Customer>()
            .name("customerItemReader")
            .resource(inputFile!!)
            .fixedLength() // 고정 길이
            .columns(
                Range(1, 11), // 길이 11, 이름
                Range(12, 12), // 길이 1, 가운데 이름의 첫 글자
                Range(13, 22), // 길이 11, 성
                Range(23, 26), // 길이 4, 주소에서 건물 번호 부분
                Range(27, 46), // 길이 20, 거주하는 거리 이름
                Range(47, 62), // 길이 16, 거주 도시
                Range(63, 64), // 길이 2 주의 두 자리 약자
                Range(65, 69) // 우편번호
            )
            .names("firstName", "middleInitial", "lastName", "addressNumber", "street", "city", "state", "zipCode")
            // 코틀린 클래스에서는 targetType()을 사용하기 좀 곤란하다...
            // .targetType(Customer::class.java)
            .fieldSetMapper(CustomerFieldSetMapper())
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
        .build();

}
