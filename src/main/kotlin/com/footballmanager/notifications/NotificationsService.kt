package com.footballmanager.notifications

import com.footballmanager.notifications.dto.NotificationInfo
import com.footballmanager.notifications.model.Notification
import org.springframework.stereotype.Service
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Service
class NotificationsService {
    private val notifications = ConcurrentHashMap<UUID, Notification>()

    fun isEmpty() = notifications.values.all { it.checked }

    fun create(notification: Notification) {
        notifications[notification.id] = notification
    }

    fun getAll(): List<NotificationInfo> {
        val notifications = notifications.values
        return notifications.map(this::convert).sortedBy { it.timestamp }
    }

    fun read(id: UUID): NotificationInfo {
        if (notifications.count() > 50) {
            notifications.values
                .sortedBy { it.timestamp }
                .takeWhile { it.checked }
                .take(20)
                .forEach { notifications.remove(it.id) }
        }
        val notification = notifications[id]!!
        notification.checked = true
        return convert(notification)
    }

    fun convert(notification: Notification): NotificationInfo =
        NotificationInfo(
            title = notification.title,
            type = notification.type,
            payload = notification.payload,
            timestamp = notification.timestamp,
            id = notification.id,
            checked = notification.checked,
            date = notification.date,
        )
}
