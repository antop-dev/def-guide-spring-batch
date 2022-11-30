package com.example.itemreader.file.multiresource

import org.springframework.batch.item.ExecutionContext
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream
import org.springframework.core.io.Resource

class CustomFileReader(
    private val delegate: ResourceAwareItemReaderItemStream<Any>
) : ResourceAwareItemReaderItemStream<Customer> {
    private var curItem: Any? = null

    override fun read(): Customer? {
        if (curItem == null) curItem = delegate.read() // Customer
        if (curItem == null) return null

        val item: Customer = curItem as Customer
        curItem = null

        while (peek() is Transaction) {
            item.transactions += curItem as Transaction
            curItem = null
        }

        return item
    }

    private fun peek(): Any? {
        if (curItem == null) curItem = delegate.read()
        return curItem
    }

    override fun open(executionContext: ExecutionContext) {
        delegate.open(executionContext)
    }

    override fun update(executionContext: ExecutionContext) {
        delegate.update(executionContext)
    }

    override fun close() {
        delegate.close()
    }

    override fun setResource(resource: Resource) {
        this.delegate.setResource(resource)
    }
}
