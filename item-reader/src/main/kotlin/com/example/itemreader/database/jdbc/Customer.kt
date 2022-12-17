package com.example.itemreader.database.jdbc

data class Customer(
    val id: Long,
    val firstName: String,
    val middleInitial: String,
    val lastName: String,
    val address: String,
    val city: String,
    val state: String,
    val zipCode: String
)
