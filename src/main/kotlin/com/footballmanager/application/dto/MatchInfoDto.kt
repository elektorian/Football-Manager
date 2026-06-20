package com.footballmanager.application.dto

import java.time.LocalDate
import java.util.UUID

data class MatchInfoDto(
    val id: UUID,
    val date: LocalDate,
    val homeTeamId: UUID,
    val homeTeamName: String,
    val homeTeamScore: String,
    val awayTeamId: UUID,
    val awayTeamName: String,
    val awayTeamScore: String,
)
