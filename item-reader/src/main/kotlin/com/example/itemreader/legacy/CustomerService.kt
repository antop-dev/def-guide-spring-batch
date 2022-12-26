package com.example.itemreader.legacy

import java.util.*

class CustomerService {
    private val customers: List<Customer>
    private var curIndex = 0

    private val generator = Random()
    private val firstNames = arrayOf("Michael", "Warren", "Ann", "Terrence", "Erica", "Laura", "Steve", "Larry")
    private val middleInitial = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private val lastNames = arrayOf("Gates", "Darrow", "Donnelly", "Jobs", "Buffett", "Ellison", "Obama")
    private val streets = arrayOf(
        "4th Street", "Wall Street", "Fifth Avenue", "Mt. Lee Drive", "Jeopardy Lane",
        "Infinite Loop Drive", "Farnam Street", "Isabella Ave", "S. Greenwood Ave"
    )
    private val cities = arrayOf("Chicago", "New York", "Hollywood", "Aurora", "Omaha", "Atherton")
    private val states = arrayOf("IL", "NY", "CA", "NE")

    init {
        customers = List(100) {
            Customer(
                generator.nextLong(),
                random(firstNames),
                randomMiddleInitial(),
                random(lastNames),
                "${generator.nextInt(9999)} ${random(streets)}",
                random(cities),
                random(states),
                "${generator.nextInt(99999)}"
            )
        }
    }

    private fun random(values: Array<String>): String {
        return values[generator.nextInt(values.size - 1)]
    }

    private fun randomMiddleInitial(): String {
        return "${middleInitial[generator.nextInt(middleInitial.length - 1)]}"
    }

    fun getCustomer(): Customer? {
        return if (curIndex < customers.size) customers[curIndex++] else null
    }

}
