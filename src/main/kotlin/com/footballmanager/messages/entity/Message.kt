package com.footballmanager.messages.entity

import com.footballmanager.messages.MessageType
import java.time.LocalDateTime
import java.util.UUID

abstract class Message(
    open val type: MessageType,
    open val id: UUID,
    open val timestamp: LocalDateTime,
    val serverTimestamp: LocalDateTime = LocalDateTime.now(),
    @Volatile
    var read: Boolean = false,
)
