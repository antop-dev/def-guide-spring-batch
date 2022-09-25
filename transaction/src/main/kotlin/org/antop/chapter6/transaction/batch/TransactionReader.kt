package org.antop.chapter6.transaction.batch

import org.antop.chapter6.transaction.domain.Transaction
import org.springframework.batch.core.StepExecution
import org.springframework.batch.core.annotation.BeforeStep
import org.springframework.batch.item.ExecutionContext
import org.springframework.batch.item.ItemStreamReader
import org.springframework.batch.item.ParseException
import org.springframework.batch.item.file.transform.FieldSet

open class TransactionReader(private val fieldSetReader: ItemStreamReader<FieldSet>) : ItemStreamReader<Transaction> {
    var recordCount = 0
    var expectedRecordCount = 0
    private lateinit var stepExecution: StepExecution;

    override fun open(executionContext: ExecutionContext) {
        fieldSetReader.open(executionContext)
    }

    override fun update(executionContext: ExecutionContext) {
        fieldSetReader.update(executionContext)
    }

    override fun close() {
        fieldSetReader.close()
    }

    override fun read(): Transaction? {
        if (recordCount == 25) {
            throw ParseException("This isn't what I hoped to happen")
        }
        return process(fieldSetReader.read())
    }

    private fun process(fieldSet: FieldSet?): Transaction? {
        var result: Transaction? = null
        fieldSet?.run {
            if (fieldCount > 1) {
                result = Transaction(
                    readString(0),
                    readDate(1, "yyyy-MM-DD HH:mm:ss"),
                    readDouble(2)
                )
                recordCount += 1
            } else {
                expectedRecordCount = fieldSet.readInt(0)
                if (expectedRecordCount != recordCount) {
                    stepExecution.setTerminateOnly()
                }
            }
        }
        return result
    }

    @BeforeStep
    fun beforeStep(execution:StepExecution) {
        this.stepExecution = execution
    }

}
