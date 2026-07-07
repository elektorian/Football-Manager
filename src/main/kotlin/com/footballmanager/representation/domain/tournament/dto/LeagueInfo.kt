package com.footballmanager.representation.domain.tournament.dto

import java.util.UUID

data class LeagueInfo(
    val leagueId: UUID,
    val leagueName: String,
    val table: Collection<TournamentTeamInfo>,
    val rounds: List<RoundInfo>?,
)