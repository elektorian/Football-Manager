package com.footballmanager.domain.model

import java.util.UUID

data class League(
    val id: UUID,
    val name: String,
    val abbreviation: String,
    val size: Int,
    val rounds: Int,
    val country: String,
    val rang: Int,
    val seasons: MutableSet<UUID> = HashSet(),
)
