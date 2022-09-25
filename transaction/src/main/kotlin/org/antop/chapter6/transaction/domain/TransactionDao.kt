package org.antop.chapter6.transaction.domain

interface TransactionDao {
    fun getTransactionsByAccountNumber(accountNumber: String): List<Transaction>
}
