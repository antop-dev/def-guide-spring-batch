package com.example.itemreader.database.mongodb

import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.data.builder.MongoItemReaderBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate

@Configuration
class MongoConfiguration(
    val stepBuilderFactory: StepBuilderFactory,
    val jobBuilderFactory: JobBuilderFactory,
) {

    @Bean
    @StepScope
    fun customerItemReader(
        mongoTemplate: MongoTemplate,
        @Value("#{jobParameters['hashTag']}") hashtag: String?
    ) = MongoItemReaderBuilder<Map<*, *>>()
        .name("tweetsItemReader")
        .targetType(Map::class.java)
        .jsonQuery("{ \"entities.hashtags.text\" : { \$eq: ?0 } }")
        .collection("tweets_collection")
        .parameterValues(hashtag)
        .pageSize(10)
        .sorts(mapOf("created_at" to Sort.Direction.ASC))
        .template(mongoTemplate)
        .build()

    @Bean
    fun itemWriter(): ItemWriter<Map<*, *>> = ItemWriter { items ->
        items.forEach { println(it) }
    }

    @Bean
    fun copyFileStep(mongoTemplate: MongoTemplate) =
        stepBuilderFactory.get("copyFileStep")
            .chunk<Map<*, *>, Map<*, *>>(10)
            .reader(customerItemReader(mongoTemplate, null))
            .writer(itemWriter())
            .build()

    @Bean
    fun copyFileJob(mongoTemplate: MongoTemplate) = jobBuilderFactory.get("copyFileJob")
        .start(copyFileStep(mongoTemplate))
        .build()

}
