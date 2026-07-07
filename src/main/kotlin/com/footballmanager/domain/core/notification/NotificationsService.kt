package com.footballmanager.domain.core.notification

import com.footballmanager.domain.core.notification.model.Notification
import com.footballmanager.domain.dao.NotificationRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class NotificationsService(
    private val notificationRepository: NotificationRepository,
) {
    fun getAll(): List<com.footballmanager.representation.domain.notification.dto.NotificationInfo> {
        val notifications = notificationRepository.findAll()
        return notifications.map(this::convert).sortedBy { it.timestamp }
    }

    fun read(id: UUID): com.footballmanager.representation.domain.notification.dto.NotificationInfo {
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

    fun convert(notification: Notification): com.footballmanager.representation.domain.notification.dto.NotificationInfo =
        _root_ide_package_.com.footballmanager.representation.domain.notification.dto.NotificationInfo(
            title = notification.title,
            type = notification.type,
            payload = notification.payload,
            timestamp = notification.timestamp,
            id = notification.id,
            checked = notification.checked,
            date = notification.date,
        )
}
