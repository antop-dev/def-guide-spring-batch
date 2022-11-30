package com.example.itemreader.file.fixedlength

import org.springframework.batch.item.file.mapping.FieldSetMapper
import org.springframework.batch.item.file.transform.FieldSet

class CustomerFieldSetMapper : FieldSetMapper<Customer> {
    override fun mapFieldSet(fieldSet: FieldSet): Customer {
        return Customer(
            fieldSet.readString("firstName"),
            fieldSet.readString("middleInitial"),
            fieldSet.readString("lastName"),
            fieldSet.readString("addressNumber"),
            fieldSet.readString("street"),
            fieldSet.readString("city"),
            fieldSet.readString("state"),
            fieldSet.readString("zipCode")
        )
    }
}
