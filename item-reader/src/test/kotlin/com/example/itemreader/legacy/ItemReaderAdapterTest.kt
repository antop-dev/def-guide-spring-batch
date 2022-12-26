package com.example.itemreader.legacy

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Test
import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.batch.test.context.SpringBatchTest
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.*

@ActiveProfiles("test")
@SpringBatchTest
@SpringBootTest(classes = [ItemReaderAdapterJob::class])
@EnableBatchProcessing
@EnableAutoConfiguration(exclude = [SqlInitializationAutoConfiguration::class])
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class ItemReaderAdapterTest(private val jobLauncherTestUtils: JobLauncherTestUtils) {
    @Test
    fun testJob() {
        val jobParameter = jobLauncherTestUtils.uniqueJobParametersBuilder.toJobParameters()
        val jobExecution = jobLauncherTestUtils.launchJob(jobParameter)
        assertThat(ExitStatus.COMPLETED, `is`(jobExecution.exitStatus))
    }
}
