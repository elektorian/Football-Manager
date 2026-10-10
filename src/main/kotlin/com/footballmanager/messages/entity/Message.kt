package com.footballmanager.messages.entity

import com.footballmanager.messages.MessageType
import java.util.UUID

abstract class Message(
    open val type: MessageType,
    open val id: UUID,
    @Volatile
    var read: Boolean = false
)
