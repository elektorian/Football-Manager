package com.footballmanager.domain.model

import java.util.UUID

data class Season(
    val id: UUID,
    val league: UUID,
    val year: Int,
    val teams: MutableSet<UUID> = HashSet(),
    val matches: MutableSet<UUID> = HashSet(),
    var schedule: UUID? = null,
)
