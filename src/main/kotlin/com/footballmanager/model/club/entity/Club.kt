package com.footballmanager.model.club.entity

import java.util.UUID
import java.util.concurrent.CopyOnWriteArraySet

data class Club(
    val id: UUID,
    val name: String,
    val players: CopyOnWriteArraySet<UUID>,
)
