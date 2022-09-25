package org.antop.chapter6.transaction.domain

data class AccountSummary(
    val id: Int = 0,
    val accountNumber: String,
    var currentBalance: Double = 0.0
)
