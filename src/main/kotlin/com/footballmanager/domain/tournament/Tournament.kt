package com.footballmanager.domain.tournament

import java.util.UUID
import java.util.concurrent.CopyOnWriteArraySet

class Tournament(
    val id: UUID,
    val name: String,
    val clubIds: CopyOnWriteArraySet<UUID>,
    val rounds: Int
)
