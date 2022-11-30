package com.example.itemreader.file.multiformat

import java.math.BigDecimal
import java.time.LocalDateTime

data class Transaction(
    val accountNumber: String,
    val transactionDate: LocalDateTime,
    val amount: BigDecimal
)
