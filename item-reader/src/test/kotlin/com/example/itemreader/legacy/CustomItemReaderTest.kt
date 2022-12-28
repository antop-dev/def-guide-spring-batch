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
@SpringBootTest(classes = [CustomItemReaderJob::class])
@EnableBatchProcessing
@EnableAutoConfiguration(exclude = [SqlInitializationAutoConfiguration::class])
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class CustomItemReaderTest(private val jobLauncherTestUtils: JobLauncherTestUtils) {
    @Test
    fun testJob() {
        // 첫번쨰 실행에서는 50번째까지 수행 후 실패한다.
        val jobParameter = jobLauncherTestUtils.uniqueJobParametersBuilder.toJobParameters()
        val jobExecution = jobLauncherTestUtils.launchJob(jobParameter)
        assertThat(ExitStatus.FAILED.exitCode, `is`(jobExecution.exitStatus.exitCode))
        // 마지막 인덱스 위치가 기록되어 있다.
        val index = jobExecution.stepExecutions
            .last()
            .executionContext.getInt("customItemReader.current.index.customers")
        assertThat(index, `is`(49))
        // 재시작 시 인덱스값 49가 들어오게 되고
        // 로직에 의해서 51번부터 시작하게 됨
        jobLauncherTestUtils.launchJob(jobParameter)
    }

}
