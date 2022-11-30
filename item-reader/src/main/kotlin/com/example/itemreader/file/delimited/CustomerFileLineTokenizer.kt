package com.example.itemreader.file.delimited

import org.springframework.batch.item.file.transform.DefaultFieldSetFactory
import org.springframework.batch.item.file.transform.FieldSet
import org.springframework.batch.item.file.transform.LineTokenizer

class CustomerFileLineTokenizer : LineTokenizer {
    private val delimiter = ","
    private val names = arrayOf("firstName", "middleInitial", "lastName", "address", "city", "state", "zipCode")
    private val filedSetFactory = DefaultFieldSetFactory()

    override fun tokenize(line: String?): FieldSet {
        if (line == null) {
            return filedSetFactory.create(arrayOf())
        }
        val fields = line.split(delimiter)
        val parsedFields = mutableListOf<String>()
        for (i in fields.indices) {
            if (i == 4) {
                // 주소 합치기
                parsedFields[i - 1] = parsedFields[i - 1] + " " + fields[i]
            } else {
                parsedFields += fields[i]
            }
        }
        return filedSetFactory.create(parsedFields.toTypedArray(), names)
    }
}
