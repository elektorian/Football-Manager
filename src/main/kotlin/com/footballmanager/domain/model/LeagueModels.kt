package com.footballmanager.domain.model

import java.time.LocalDate

data class Team(
    val id: String,
    val name: String
)

data class League(
    val id: String,
    val name: String,
    val teamIds: List<String>
)

data class MatchResult(
    val scoreA: Int,
    val scoreB: Int
)

data class Match(
    val id: String,
    val leagueId: String,
    val round: Int,
    val teamAId: String,
    val teamBId: String,
    val scheduledDate: LocalDate,
    var result: MatchResult? = null
)