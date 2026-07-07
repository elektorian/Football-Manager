package com.footballmanager.domain.core.notification.model

import com.footballmanager.domain.core.notification.enumeration.NotificationType
import com.footballmanager.domain.core.notification.payload.NotificationPayload
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class Notification(
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val type: NotificationType,
    val payload: NotificationPayload,
    val timestamp: LocalDateTime,
    @Volatile
    var checked: Boolean = false,
    val date: LocalDate,
)
