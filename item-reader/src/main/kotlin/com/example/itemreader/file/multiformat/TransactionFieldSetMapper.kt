package com.example.itemreader.file.multiformat

import org.springframework.batch.item.file.mapping.FieldSetMapper
import org.springframework.batch.item.file.transform.FieldSet
import java.time.ZoneId

class TransactionFieldSetMapper : FieldSetMapper<Any> {
    override fun mapFieldSet(fieldSet: FieldSet): Transaction {
        return Transaction(
            fieldSet.readString("accountNumber"),
            fieldSet.readDate("transactionDate", "yyyy-MM-dd HH:mm:ss")
                .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
            fieldSet.readBigDecimal("amount")
        )
    }
}
