package com.example.itemreader.jdbc

import com.example.itemreader.database.jdbc.paging.JdbcPagingConfiguration
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Test
import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.JobParameter
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.batch.test.context.SpringBatchTest
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.jdbc.Sql

@ActiveProfiles("test")
@SpringBatchTest
@EnableBatchProcessing
@EnableAutoConfiguration
@ContextConfiguration(classes = [JdbcPagingConfiguration::class])
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Sql(scripts = ["classpath:schema-h2.sql", "classpath:data-h2.sql"])
class JdbcPagingItemReaderTest(private val jobLauncherTestUtils: JobLauncherTestUtils) {
    @Test
    fun testJob() {
        val jobParameter = jobLauncherTestUtils.uniqueJobParametersBuilder
            .addParameter("city", JobParameter("Gary"))
            .toJobParameters()
        val jobExecution = jobLauncherTestUtils.launchJob(jobParameter)
        assertThat(ExitStatus.COMPLETED, `is`(jobExecution.exitStatus))
    }
}
