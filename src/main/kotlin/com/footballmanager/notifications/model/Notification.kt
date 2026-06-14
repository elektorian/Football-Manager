package com.footballmanager.notifications.model

import java.time.LocalDateTime

data class Notification(
    val title: String,
    val text: String,
    val timestamp: LocalDateTime,
)
