package com.example.itemreader.file.json

import com.fasterxml.jackson.annotation.JsonFormat
import java.math.BigDecimal
import java.time.LocalDateTime

data class Transaction(
    val accountNumber: String,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val transactionDate: LocalDateTime,
    val amount: BigDecimal
)
