package com.footballmanager.tournaments.dto

import java.util.Date
import java.util.UUID

data class MatchInfo(
    val id: UUID,
    val date: Date,
    val homeTeamId: UUID,
    val homeTeamName: String,
    val homeTeamScore : String,
    val awayTeamId: UUID,
    val awayTeamName: String,
    val awayTeamScore : String,
)
