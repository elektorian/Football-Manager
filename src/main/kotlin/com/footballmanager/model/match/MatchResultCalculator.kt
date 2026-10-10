package com.footballmanager.model.match

import com.footballmanager.model.match.entity.Match
import com.footballmanager.model.match.entity.MatchResult
import com.footballmanager.model.match.entity.MatchTeamResult
import org.springframework.stereotype.Component
import kotlin.random.Random

@Component
class MatchResultCalculator {
    fun calculate(match: Match): MatchResult {
        val homeScored = Random.nextInt(0, 4)
        val awayScored = Random.nextInt(0, 4)
        val homeTeamResult = MatchTeamResult(
            clubId = match.homeTeam,
            scored = homeScored,
        )
        val awayTeamResult = MatchTeamResult(
            clubId = match.awayTeam,
            scored = awayScored,
        )
        val matchResult = MatchResult(
            homeTeamResult = homeTeamResult,
            awayTeamResult = awayTeamResult,
        )
        match.result = matchResult
        return matchResult
    }
}