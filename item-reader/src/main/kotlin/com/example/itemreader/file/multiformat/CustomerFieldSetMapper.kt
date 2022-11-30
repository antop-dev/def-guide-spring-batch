package com.example.itemreader.file.multiformat

import org.springframework.batch.item.file.mapping.FieldSetMapper
import org.springframework.batch.item.file.transform.FieldSet

class CustomerFieldSetMapper : FieldSetMapper<Any> {
    override fun mapFieldSet(fieldSet: FieldSet): Customer {
        return Customer(
            fieldSet.readString("firstName"),
            fieldSet.readString("middleInitial"),
            fieldSet.readString("lastName"),
            fieldSet.readString("address"),
            fieldSet.readString("city"),
            fieldSet.readString("state"),
            fieldSet.readString("zipCode")
        )
    }
}
