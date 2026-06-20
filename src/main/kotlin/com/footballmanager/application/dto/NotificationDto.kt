package com.footballmanager.application.dto

import com.footballmanager.domain.model.NotificationPayload
import com.footballmanager.domain.model.NotificationType
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class NotificationDto(
    val id: UUID,
    val title: String,
    val type: NotificationType,
    val payload: NotificationPayload,
    val timestamp: LocalDateTime,
    val checked: Boolean,
    val date: LocalDate,
)
