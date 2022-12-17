package com.example.itemreader.database.jdbc

import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class CustomerRowMapper : RowMapper<Customer> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Customer? {
        return Customer(
            id = rs.getLong("id"),
            firstName = rs.getString("firstName"),
            middleInitial = rs.getString("middleInitial"),
            lastName = rs.getString("lastName"),
            address = rs.getString("address"),
            city = rs.getString("city"),
            state = rs.getString("state"),
            zipCode = rs.getString("zipCode")
        )
    }
}
