package org.antop.chapter6.transaction.batch

import org.antop.chapter6.transaction.domain.AccountSummary
import org.antop.chapter6.transaction.domain.TransactionDao
import org.springframework.batch.item.ItemProcessor

open class TransactionApplierProcessor(private val transactionDao: TransactionDao) :
    ItemProcessor<AccountSummary, AccountSummary> {

    override fun process(item: AccountSummary): AccountSummary {
        val transactions = transactionDao.getTransactionsByAccountNumber(item.accountNumber)
        item.currentBalance += transactions.sumOf { it.amount }
        return item
    }
}
