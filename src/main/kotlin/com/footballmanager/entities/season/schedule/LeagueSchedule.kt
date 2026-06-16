package com.footballmanager.entities.season.schedule

import java.util.UUID

data class LeagueSchedule(
    val id: UUID,
    val rounds: List<UUID>,
)
