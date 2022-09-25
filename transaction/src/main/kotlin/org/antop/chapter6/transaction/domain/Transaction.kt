package org.antop.chapter6.transaction.domain

import java.util.*

data class Transaction(
    val accountNumber: String,
    val timestamp: Date,
    val amount: Double
)
