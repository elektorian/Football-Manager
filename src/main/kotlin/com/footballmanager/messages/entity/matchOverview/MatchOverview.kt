package com.footballmanager.messages.entity.matchOverview

import java.time.LocalDate
import java.util.UUID

data class MatchOverview(
    val matchId: UUID,
    val homeTeamName: String,
    val awayTeamName: String,
    val homeTeamScore : Int,
    val awayTeamScore : Int,
    val date: LocalDate,
)
