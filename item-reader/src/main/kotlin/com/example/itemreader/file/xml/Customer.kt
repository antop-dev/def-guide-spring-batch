package com.example.itemreader.file.xml

import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlElementWrapper
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "customer")
data class Customer(
    @field:XmlElement(name = "firstName")
    val firstName: String,
    @field:XmlElement(name = "middleInitial")
    val middleInitial: String,
    @field:XmlElement(name = "lastName")
    val lastName: String,
    @field:XmlElement(name = "addressNumber")
    val addressNumber: String,
    @field:XmlElement(name = "street")
    val street: String,
    @field:XmlElement(name = "city")
    val city: String,
    @field:XmlElement(name = "state")
    val state: String,
    @field:XmlElement(name = "zipCode")
    val zipCode: String,
    @field:XmlElementWrapper(name = "transactions")
    @field:XmlElement(name = "transaction")
    val transactions: List<Transaction>?
) {
    override fun toString(): String {
        return "$firstName $middleInitial $lastName has " +
                if (!transactions.isNullOrEmpty()) "${transactions.size} transactions" else "no transactions."
    }
}
