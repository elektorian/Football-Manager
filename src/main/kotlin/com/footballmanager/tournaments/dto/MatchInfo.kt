package com.footballmanager.tournaments.dto

import java.time.LocalDate
import java.util.UUID

data class MatchInfo(
    val id: UUID,
    val date: LocalDate,
    val homeTeamId: UUID,
    val homeTeamName: String,
    val homeTeamScore : String,
    val awayTeamId: UUID,
    val awayTeamName: String,
    val awayTeamScore : String,
)
