package com.footballmanager.model.match.entity

import java.util.UUID

data class MatchResult(
    val homeTeamResult: MatchTeamResult,
    val awayTeamResult: MatchTeamResult,
) {
    fun isWinner(teamId: UUID): Boolean {
        if (homeTeamResult.clubId == teamId) {
            return homeTeamResult.scored > awayTeamResult.scored
        } else if (awayTeamResult.clubId == teamId) {
            return awayTeamResult.scored > homeTeamResult.scored
        }
        throw IllegalStateException()
    }

    fun isLoser(teamId: UUID): Boolean {
        if (homeTeamResult.clubId == teamId) {
            return homeTeamResult.scored < awayTeamResult.scored
        } else if (awayTeamResult.clubId == teamId) {
            return awayTeamResult.scored < homeTeamResult.scored
        }
        throw IllegalStateException()
    }

    fun isDraw(): Boolean {
        return homeTeamResult.scored == awayTeamResult.scored
    }

    fun getScored(teamId: UUID): Int {
        if (homeTeamResult.clubId == teamId) {
            return homeTeamResult.scored
        } else if (awayTeamResult.clubId == teamId) {
            return awayTeamResult.scored
        }
        throw IllegalStateException()
    }

    fun getConceded(teamId: UUID): Int {
        if (homeTeamResult.clubId == teamId) {
            return awayTeamResult.scored
        } else if (awayTeamResult.clubId == teamId) {
            return homeTeamResult.scored
        }
        throw IllegalStateException()
    }
}
