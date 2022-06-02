package org.antop.helloworld

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@EnableBatchProcessing
@SpringBootApplication
class HelloWorldApplication(val jobBuilderFactory: JobBuilderFactory, val stepBuilderFactory: StepBuilderFactory) {

    @Bean
    fun step() = stepBuilderFactory.get("step1").tasklet { _, _ ->
        println("Hello, World!")
        RepeatStatus.FINISHED
    }.build()

    @Bean
    fun job() = jobBuilderFactory.get("job").start(step()).build()

}

fun main(args: Array<String>) {
    runApplication<HelloWorldApplication>(*args)
}
