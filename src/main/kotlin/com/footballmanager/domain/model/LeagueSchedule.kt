package com.footballmanager.domain.model

import java.util.UUID

data class LeagueSchedule(
    val id: UUID,
    val rounds: List<UUID>,
)
