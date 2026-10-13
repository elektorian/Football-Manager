package com.footballmanager.tournaments.model

import java.util.UUID

data class LeagueInfo(
    val leagueId: UUID,
    val name: String,
    val year: Int,
    val teams: List<LeagueTeamInfo>,
)
