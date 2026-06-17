package com.footballmanager.notifications.dto

import java.time.LocalDateTime
import java.util.UUID

data class NotificationInfo(
    val id: UUID,
    val title: String,
    val text: String,
    val timestamp: LocalDateTime,
    val checked: Boolean,
)
