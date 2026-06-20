package com.footballmanager.application.dto

import java.util.UUID

data class LeagueInfoDto(
    val leagueId: UUID,
    val leagueName: String,
    val table: Collection<LeagueTeamInfoDto>,
    val rounds: List<RoundInfoDto>?,
)
