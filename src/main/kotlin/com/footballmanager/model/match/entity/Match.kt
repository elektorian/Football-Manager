package com.footballmanager.model.match.entity

import java.time.LocalDate
import java.util.UUID

data class Match(
    val id: UUID,
    val date: LocalDate,
    val homeTeam: UUID,
    val awayTeam: UUID,
    @Volatile
    var result: MatchResult? = null,
)