package com.footballmanager.model.tournament.entity

import java.util.UUID
import java.util.concurrent.CopyOnWriteArrayList

data class Tournament(
    val id: UUID,
    val name: String,
    val size: Int,
    val seasons: CopyOnWriteArrayList<UUID>,
)
