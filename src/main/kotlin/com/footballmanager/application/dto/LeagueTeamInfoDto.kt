package com.footballmanager.application.dto

import java.util.UUID

data class LeagueTeamInfoDto(
    val teamId: UUID,
    val name: String,
    val victories: Int,
    val draws: Int,
    val losses: Int,
    val goalsScored: Int,
    val goalsConceded: Int,
    val position: Int,
    val points: Int,
)
