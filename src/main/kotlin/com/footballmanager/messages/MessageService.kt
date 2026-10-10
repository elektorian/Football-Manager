package com.footballmanager.messages

import com.footballmanager.messages.entity.Message
import org.springframework.stereotype.Component
import java.util.LinkedList
import java.util.UUID

@Component
class MessageService {
    private val queue = LinkedList<Message>()

    @Synchronized
    fun register(message: Message) {
        if (queue.size >= 50) {
            val iterator = queue.listIterator()
            while (iterator.hasNext() && queue.size >= 50) {
                val oldest = iterator.next()
                if (!oldest.read) break
                queue.removeFirst()
            }
        }
        queue.add(message)
    }

    @Synchronized
    fun read(id: UUID) {
        val message = queue.find { it.id == id }!!
        message.read = true
    }

    @Synchronized
    fun getAll(): List<Message> {
        return queue.toList()
    }

    @Synchronized
    fun hasUnread(): Boolean {
        return queue.any { !it.read }
    }
}
