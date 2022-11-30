package com.example.itemreader.file.xml

import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import org.springframework.oxm.jaxb.Jaxb2Marshaller

@Configuration
class XmlConfiguration(
    val stepBuilderFactory: StepBuilderFactory,
    val jobBuilderFactory: JobBuilderFactory
) {

    @Bean
    @StepScope
    fun customerItemReader(@Value("#{jobParameters['customerFile']}") inputFile: Resource?) =
        StaxEventItemReaderBuilder<Customer>()
            .name("customerFileReader")
            .resource(inputFile!!)
            .addFragmentRootElements("customer")
            .unmarshaller(customerMarshaller())
            .build()

    private fun customerMarshaller() = Jaxb2Marshaller().apply {
        setClassesToBeBound(Customer::class.java, Transaction::class.java)
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
