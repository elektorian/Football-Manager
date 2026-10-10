package com.footballmanager.messages.entity.matchOverview

import com.footballmanager.messages.MessageType
import com.footballmanager.messages.entity.Message
import java.time.LocalDateTime
import java.util.*

data class MatchOverviewMessage(
    override val type: MessageType,
    override val id: UUID,
    override val timestamp: LocalDateTime,
    override val title: String,
    val matches: List<MatchOverview>,
) : Message(
    type = type,
    id = id,
    timestamp = timestamp,
    title = title,
)
