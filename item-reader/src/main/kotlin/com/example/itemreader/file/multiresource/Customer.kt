package com.example.itemreader.file.multiresource

data class Customer (
    val firstName: String,
    val middleInitial: String,
    val lastName: String,
    val address: String,
    val city: String,
    val state: String,
    val zipCode: String
) {
    val transactions = mutableListOf<Transaction>()
    override fun toString(): String {
        return "Customer(firstName='$firstName', middleInitial='$middleInitial', lastName='$lastName', address='$address', city='$city', state='$state', zipCode='$zipCode', transactions=${transactions.size})"
    }


}
