package com.footballmanager.notifications

import com.footballmanager.application.repository.NotificationRepository
import com.footballmanager.notifications.dto.NotificationInfo
import com.footballmanager.notifications.model.Notification
import org.springframework.stereotype.Service
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Service
class NotificationsService(
    private val notificationRepository: NotificationRepository,
) {
    fun getAll(): List<NotificationInfo> {
        val notifications = notificationRepository.findAll()
        return notifications.map(this::convert).sortedBy { it.timestamp }
    }

    fun read(id: UUID): NotificationInfo {
        if (notificationRepository.count() > 50) {
            notificationRepository.findAll()
                .sortedBy { it.timestamp }
                .takeWhile { it.checked }
                .take(20)
                .forEach { notificationRepository.delete(it.id) }
        }
        val notification = notificationRepository.get(id)
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
