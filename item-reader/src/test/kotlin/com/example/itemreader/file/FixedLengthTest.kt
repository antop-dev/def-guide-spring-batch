package com.example.itemreader.file

import com.example.itemreader.fixedlength.FixedLengthConfiguration
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

@ActiveProfiles("test")
@SpringBatchTest
@EnableBatchProcessing
@EnableAutoConfiguration
@ContextConfiguration(classes = [FixedLengthConfiguration::class])
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class FixedLengthTest(private val jobLauncherTestUtils: JobLauncherTestUtils) {
    @Test
    fun testJob() {
        val jobParameter = jobLauncherTestUtils.uniqueJobParametersBuilder
            .addParameter("customerFile", JobParameter("classpath:input/customerFixedWidth.txt"))
            .toJobParameters()
        val jobExecution = jobLauncherTestUtils.launchJob(jobParameter)
        assertThat(ExitStatus.COMPLETED, `is`(jobExecution.exitStatus))
    }

}
