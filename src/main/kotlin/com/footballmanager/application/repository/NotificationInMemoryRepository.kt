package com.footballmanager.application.repository

import com.footballmanager.domain.repository.NotificationRepository
import com.footballmanager.notifications.model.Notification
import org.springframework.stereotype.Repository
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Repository
class NotificationInMemoryRepository : NotificationRepository {
    private val notifications = ConcurrentHashMap<UUID, Notification>()

    override fun save(notification: Notification) {
        notifications[notification.id] = notification
    }

    override fun findAll(): List<Notification> {
        return notifications.values.toList()
    }

    override fun count(): Int {
        return notifications.size
    }

    override fun isEmpty(): Boolean {
        return notifications.isEmpty()
    }

    override fun get(id: UUID): Notification {
        return notifications[id]!!
    }

    override fun delete(id: UUID) {
        notifications.remove(id)
    }
}