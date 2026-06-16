package com.footballmanager.entities.match

import com.footballmanager.entities.Club
import java.util.Date
import java.util.UUID

data class Match(
    val id: UUID,
    val date: Date,
    val homeTeam: UUID,
    val awayTeam: UUID,
    val homeTeamResult: MatchTeamResult? = null,
    val awayTeamResult: MatchTeamResult? = null,
    val round: UUID,
) {
    fun passed(): Boolean{
        return !(awayTeamResult == null || homeTeamResult == null)
    }

    fun getResult(team: Club): MatchTeamResult {
        if (awayTeamResult == null || homeTeamResult == null) throw IllegalStateException("awayTeamResult == null or homeTeamResult == null")
        val teamResult =
            when (id) {
                homeTeamResult.team -> homeTeamResult
                awayTeamResult.team -> awayTeamResult
                else -> throw IllegalStateException("$this match result is not connected to $team team")
            }
        return teamResult
    }
}