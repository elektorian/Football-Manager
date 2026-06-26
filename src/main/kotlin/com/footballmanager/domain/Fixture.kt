package com.footballmanager.domain

import java.time.LocalDate

data class Fixture(
    val id: Long,
    val competitionId: Long,
    val date: LocalDate,
    val homeClubId: Long,
    val awayClubId: Long,
    var played: Boolean = false,
    var homeGoals: Int? = null,
    var awayGoals: Int? = null
) {
    val result: MatchResult?
        get() {
            if (!played) return null
            val hg = homeGoals ?: return null
            val ag = awayGoals ?: return null
            return MatchResult(hg, ag)
        }
}

data class MatchResult(
    val homeGoals: Int,
    val awayGoals: Int
) {
    val isHomeWin: Boolean get() = homeGoals > awayGoals
    val isAwayWin: Boolean get() = awayGoals > homeGoals
    val isDraw: Boolean get() = homeGoals == awayGoals
}
