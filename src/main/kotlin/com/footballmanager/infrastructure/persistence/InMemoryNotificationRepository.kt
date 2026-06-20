package com.footballmanager.infrastructure.persistence

import com.footballmanager.domain.model.Notification
import com.footballmanager.domain.repository.NotificationRepository
import org.springframework.stereotype.Repository
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Repository
class InMemoryNotificationRepository : NotificationRepository {
    private val store = ConcurrentHashMap<UUID, Notification>()

    override fun findAll(): List<Notification> = store.values.toList()

    override fun findById(id: UUID): Notification? = store[id]

    override fun save(notification: Notification) {
        store[notification.id] = notification
    }

    override fun deleteOldestChecked(count: Int) {
        if (store.size <= count) return
        store.values
            .sortedBy { it.timestamp }
            .takeWhile { it.checked }
            .forEach { store.remove(it.id) }
    }
}
