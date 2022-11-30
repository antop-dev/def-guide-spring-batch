package com.example.itemreader

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@EnableBatchProcessing
@SpringBootApplication
class ItemReaderApplication

fun main(args: Array<String>) {
    runApplication<ItemReaderApplication>(*args)
}
