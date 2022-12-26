package com.example.itemreader.jdbc

import com.example.itemreader.database.mongodb.MongoConfiguration
import org.bson.Document
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.JobParameter
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.batch.test.context.SpringBatchTest
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationAutoConfiguration
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor
import org.springframework.util.ResourceUtils
import kotlin.io.path.readLines


@ActiveProfiles("test")
@SpringBootTest(classes = [MongoConfiguration::class])
@SpringBatchTest
@EnableBatchProcessing
@EnableAutoConfiguration(exclude = [SqlInitializationAutoConfiguration::class])
@AutoConfigureDataMongo
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@DirtiesContext
class MongoItemReaderTest(
    private val jobLauncherTestUtils: JobLauncherTestUtils,
    val mongoTemplate: MongoTemplate
) {

    @BeforeEach
    fun setUp() {
        val resource = ResourceUtils.getFile("classpath:tweets.json")
        val documents = resource.toPath().readLines().map { Document.parse(it) }
        mongoTemplate.createCollection("tweets_collection").insertMany(documents)
    }

    @AfterEach
    fun tearDown() {
        mongoTemplate.dropCollection("tweets_collection")
    }

    @Test
    fun testJob() {
        // given
        val hashtag = "webinar"
        val jobParameter = jobLauncherTestUtils.uniqueJobParametersBuilder
            .addParameter("hashTag", JobParameter(hashtag))
            .toJobParameters()
        // when
        val jobExecution = jobLauncherTestUtils.launchJob(jobParameter)
        // then
        assertThat(ExitStatus.COMPLETED, `is`(jobExecution.exitStatus))
    }
}
