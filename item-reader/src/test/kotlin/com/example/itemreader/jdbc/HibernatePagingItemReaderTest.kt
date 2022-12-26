package com.example.itemreader.jdbc

import com.example.itemreader.database.jpa.HibernatePagingConfiguration
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Test
import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.JobParameter
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.batch.test.context.SpringBatchTest
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor

@ActiveProfiles("test")
@SpringBootTest(classes = [HibernatePagingConfiguration::class])
@SpringBatchTest
@EnableBatchProcessing
@EnableAutoConfiguration
@EntityScan("com.example.itemreader.database.jpa")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@DirtiesContext
class HibernatePagingItemReaderTest(private val jobLauncherTestUtils: JobLauncherTestUtils) {
    @Test
    fun testJob() {
        val jobParameter = jobLauncherTestUtils.uniqueJobParametersBuilder
            .addParameter("city", JobParameter("Portland"))
            .toJobParameters()
        val jobExecution = jobLauncherTestUtils.launchJob(jobParameter)
        assertThat(ExitStatus.COMPLETED, `is`(jobExecution.exitStatus))
    }
}
