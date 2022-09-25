package org.antop.chapter6.transaction.domain

import org.springframework.jdbc.core.JdbcTemplate
import javax.sql.DataSource

open class TransactionDaoSupport(dataSource: DataSource) : JdbcTemplate(dataSource), TransactionDao {

    override fun getTransactionsByAccountNumber(accountNumber: String): List<Transaction> {
        return query("select t.id, t.timestamp, t.amount " +
                "from transaction t inner join account_summary a on " +
                "a.id = t.account_summary_id " +
                "where a.account_number = ?",
            arrayOf<Any>(accountNumber)
        ) { rs, _ ->
            Transaction(
                accountNumber,
                rs.getDate("timestamp"),
                rs.getDouble("amount")
            )
        }
    }
}
