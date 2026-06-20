package com.footballmanager.tournaments.dto

import java.util.UUID

data class LeagueInfo(
    val leagueId: UUID,
    val leagueName: String,
    val table: Collection<LeagueTeamInfo>,
    val rounds: List<RoundInfo>?,
)