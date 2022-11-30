package com.example.itemreader.file.xml

import java.math.BigDecimal
import java.time.LocalDateTime
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter

@XmlRootElement(name = "transaction")
data class Transaction(
    @field:XmlElement(name = "accountNumber")
    val accountNumber: String,
    @field:XmlElement(name = "transactionDate")
    @field:XmlJavaTypeAdapter(value = LocalDateTimeAdapter::class)
    val transactionDate: LocalDateTime,
    @field:XmlElement(name = "amount")
    val amount: BigDecimal
)
