package com.footballmanager.notifications.dto

import com.footballmanager.notifications.model.NotificationType
import com.footballmanager.notifications.payload.NotificationPayload
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class NotificationInfo(
    val id: UUID,
    val title: String,
    val type: NotificationType,
    val payload: NotificationPayload,
    val timestamp: LocalDateTime,
    val checked: Boolean,
    val date: LocalDate,
)
