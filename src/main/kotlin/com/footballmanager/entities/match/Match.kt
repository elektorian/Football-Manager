package com.footballmanager.entities.match

import com.footballmanager.entities.Club
import java.time.LocalDate
import java.util.Date
import java.util.UUID

data class Match(
    val id: UUID,
    val date: LocalDate,
    val homeTeam: UUID,
    val awayTeam: UUID,
    val round: UUID,
) {
    var homeTeamResult: MatchTeamResult? = null
        @Synchronized
        get
        @Synchronized
        set
    var awayTeamResult: MatchTeamResult? = null
        @Synchronized
        get
        @Synchronized
        set

    @Synchronized
    fun passed(): Boolean {
        return !(awayTeamResult == null || homeTeamResult == null)
    }

    @Synchronized
    fun getResult(team: Club): MatchTeamResult {
        if (awayTeamResult == null || homeTeamResult == null) throw IllegalStateException("awayTeamResult == null or homeTeamResult == null")
        val teamResult =
            when (team.id) {
                homeTeamResult!!.team -> homeTeamResult
                awayTeamResult!!.team -> awayTeamResult
                else -> throw IllegalStateException("$this match result is not connected to $team team")
            }
        return teamResult!!
    }
}