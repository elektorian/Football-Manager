package com.footballmanager.notifications.model

import java.time.LocalDateTime
import java.util.UUID

data class Notification(
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val text: String,
    val timestamp: LocalDateTime,
    @Volatile
    var checked: Boolean = false,
)
