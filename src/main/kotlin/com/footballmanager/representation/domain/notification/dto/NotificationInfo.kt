package com.footballmanager.representation.domain.notification.dto

import com.footballmanager.domain.core.notification.enumeration.NotificationType
import com.footballmanager.domain.core.notification.payload.NotificationPayload
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class NotificationInfo(
    val id: UUID,
    val title: String,
    val type: NotificationType,
    val payload: NotificationPayload,
    val timestamp: LocalDateTime,
    val checked: Boolean,
    val date: LocalDate,
)
