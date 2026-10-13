package com.footballmanager.tournaments.model

import java.util.UUID

data class LeagueTeamInfo(
    val teamId: UUID,
    val name: String,
    val position: Int,
    val matches: Int,
    val wins: Int,
    val draws: Int,
    val losses: Int,
    val scored: Int,
    val conceded: Int,
    val diff: Int,
    val points: Int,
)
