package com.footballmanager.application.port.input

import com.footballmanager.application.dto.NotificationDto
import java.util.UUID

interface NotificationUseCase {
    fun getAll(): List<NotificationDto>
    fun read(id: UUID): NotificationDto
}
