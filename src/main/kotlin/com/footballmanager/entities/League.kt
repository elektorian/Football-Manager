package com.footballmanager.entities

import com.footballmanager.entities.match.Match
import java.util.UUID
import java.util.concurrent.CopyOnWriteArraySet

data class League(
    val id: UUID,
    val name: String,
    val abbreviation: String,
    val size: Int,
    val rounds: Int,
    val country: String,
    val rang: Int,
    val seasons: CopyOnWriteArraySet<Season>,
)
