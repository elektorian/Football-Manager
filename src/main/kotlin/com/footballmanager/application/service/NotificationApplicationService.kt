package com.footballmanager.application.service

import com.footballmanager.application.dto.NotificationDto
import com.footballmanager.application.port.input.NotificationUseCase
import com.footballmanager.domain.model.Notification
import com.footballmanager.domain.repository.NotificationRepository
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class NotificationApplicationService(
    private val notificationRepository: NotificationRepository,
) : NotificationUseCase {

    override fun getAll(): List<NotificationDto> {
        return notificationRepository.findAll()
            .map { toDto(it) }
            .sortedBy { it.timestamp }
    }

    override fun read(id: UUID): NotificationDto {
        notificationRepository.deleteOldestChecked(30)
        val notification = notificationRepository.findById(id)
            ?: throw IllegalStateException("Notification not found")
        notification.checked = true
        return toDto(notification)
    }

    private fun toDto(n: Notification) = NotificationDto(
        id = n.id,
        title = n.title,
        type = n.type,
        payload = n.payload,
        timestamp = n.timestamp,
        checked = n.checked,
        date = n.date,
    )
}
