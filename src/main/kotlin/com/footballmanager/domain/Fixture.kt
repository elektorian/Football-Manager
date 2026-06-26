package com.footballmanager.domain

import java.time.LocalDate

data class Fixture(
    val id: Long,
    val competitionId: Long,
    val date: LocalDate,
    val homeClubId: Long,
    val awayClubId: Long,
    val played: Boolean = false,
    val homeGoals: Int? = null,
    val awayGoals: Int? = null
) {
    val result: MatchResult?
        get() = if (played && homeGoals != null && awayGoals != null)
            MatchResult(homeGoals, awayGoals)
        else null
}

data class MatchResult(
    val homeGoals: Int,
    val awayGoals: Int
) {
    val isHomeWin: Boolean get() = homeGoals > awayGoals
    val isAwayWin: Boolean get() = awayGoals > homeGoals
    val isDraw: Boolean get() = homeGoals == awayGoals
}
