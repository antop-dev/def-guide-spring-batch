package com.example.itemreader.database.jpa

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "customer")
class Customer(
    @Id
    val id: Long,
    @Column(name = "firstName")
    val firstName: String,
    @Column(name = "middleInitial")
    val middleInitial: String,
    @Column(name = "lastName")
    val lastName: String,
    @Column(name = "address")
    val address: String,
    @Column(name = "city")
    val city: String,
    @Column(name = "state")
    val state: String,
    @Column(name = "zipCode")
    val zipCode: String
) {
    override fun toString(): String {
        return "Customer(id=$id, firstName='$firstName', middleInitial='$middleInitial', lastName='$lastName', address='$address', city='$city', state='$state', zipCode='$zipCode')"
    }
}
