package com.example.itemreader.legacy

import org.springframework.batch.item.ExecutionContext
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemStreamSupport
import java.util.*

open class CustomItemReader : ItemStreamSupport(), ItemReader<Customer> {
    private val customers: List<Customer>
    private var curIndex = 0

    private val generator: Random = Random()
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
        this.customers = List(100) { buildCustomer(it) }
    }

    override fun read(): Customer? {
        return if (curIndex < customers.size) {
            if (curIndex == 50) {
                throw RuntimeException("This will end your execution.")
            }
            customers[curIndex++]
        } else {
            null
        }
    }

    private fun buildCustomer(index: Int): Customer {
        return Customer(
            index.toLong(),
            random(firstNames),
            randomMiddleInitial(),
            random(lastNames),
            "${generator.nextInt(9999)} ${random(streets)}",
            random(cities),
            random(states),
            "${generator.nextInt(99999)}"
        )
    }

    private fun random(values: Array<String>): String {
        return values[generator.nextInt(values.size - 1)]
    }

    private fun randomMiddleInitial(): String {
        return "${middleInitial[generator.nextInt(middleInitial.length - 1)]}"
    }

    companion object {
        const val INDEX_KEY = "current.index.customers"
    }

    override fun open(executionContext: ExecutionContext) {
        val key = getExecutionContextKey(INDEX_KEY)
        curIndex = if (executionContext.containsKey(key)) {
            val index = executionContext.getInt(key)
            // 시작 시 49 인덱스 값이 오면 51인덱스로 건너 뛴다.
            if (index == 49) 51 else index
        } else {
            0
        }
    }

    override fun update(executionContext: ExecutionContext) {
        val key = getExecutionContextKey(INDEX_KEY)
        executionContext.putInt(key, curIndex)
    }

}
