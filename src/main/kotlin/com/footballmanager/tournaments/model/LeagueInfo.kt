package com.footballmanager.tournaments.model

import java.util.UUID

data class LeagueInfo(
    val leagueId: UUID,
    val name: String,
    val teams: List<LeagueTeamInfo>,
)
