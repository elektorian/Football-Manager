package com.footballmanager.domain.service

import com.footballmanager.domain.model.Match
import com.footballmanager.domain.model.MatchTeamResult
import com.footballmanager.domain.model.MatchTeamStatus
import org.springframework.stereotype.Component
import kotlin.random.Random

@Component
class MatchesEngine {
    fun playMatch(match: Match) {
        val homeScored = Random.nextInt(0, 4)
        val awayScored = Random.nextInt(0, 4)
        val (homeStatus, awayStatus) = when {
            homeScored == awayScored -> MatchTeamStatus.DRAW to MatchTeamStatus.DRAW
            homeScored > awayScored -> MatchTeamStatus.WINNER to MatchTeamStatus.LOSER
            else -> MatchTeamStatus.LOSER to MatchTeamStatus.WINNER
        }
        match.homeTeamResult = MatchTeamResult(
            team = match.homeTeam,
            scored = homeScored,
            conceded = awayScored,
            status = homeStatus,
        )
        match.awayTeamResult = MatchTeamResult(
            team = match.awayTeam,
            scored = awayScored,
            conceded = homeScored,
            status = awayStatus,
        )
    }
}
