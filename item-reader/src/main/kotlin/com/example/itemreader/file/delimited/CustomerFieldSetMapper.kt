package com.example.itemreader.file.delimited

import org.springframework.batch.item.file.mapping.FieldSetMapper
import org.springframework.batch.item.file.transform.FieldSet

class CustomerFieldSetMapper : FieldSetMapper<com.example.itemreader.file.delimited.Customer> {
    override fun mapFieldSet(fieldSet: FieldSet): com.example.itemreader.file.delimited.Customer {
        return com.example.itemreader.file.delimited.Customer(
            fieldSet.readString("firstName"),
            fieldSet.readString("middleInitial"),
            fieldSet.readString("lastName"),
            fieldSet.readString("addressNumber") + " " + fieldSet.readString("street"),
            fieldSet.readString("city"),
            fieldSet.readString("state"),
            fieldSet.readString("zipCode")
        )
    }
}
