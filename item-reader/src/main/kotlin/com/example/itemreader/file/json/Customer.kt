package com.example.itemreader.file.json

data class Customer(
    val firstName: String,
    val middleInitial: String,
    val lastName: String,
    val address: String,
    val city: String,
    val state: String,
    val zipCode: String,
    val transactions: List<Transaction>?
) {
    override fun toString(): String {
        return "$firstName $middleInitial $lastName has " +
                if (!transactions.isNullOrEmpty()) "${transactions.size} transactions" else "no transactions."
    }
}
