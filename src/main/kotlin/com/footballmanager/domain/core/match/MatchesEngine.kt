package com.footballmanager.domain.core.match

import com.footballmanager.domain.core.match.enumeration.MatchTeamStatus
import com.footballmanager.domain.core.match.model.Match
import com.footballmanager.domain.core.match.model.MatchTeamResult
import com.footballmanager.domain.dao.MatchRepository
import com.footballmanager.domain.dao.TournamentRepository
import com.footballmanager.domain.functions.TournamentCurrentRoundFunction
import com.footballmanager.domain.functions.TournamentTodayMatchesFunction
import org.springframework.stereotype.Component
import kotlin.random.Random

@Component
class MatchesEngine(
    private val tournamentRepository: TournamentRepository,
    private val tournamentTodayMatchesFunction: TournamentTodayMatchesFunction,
    private val tournamentCurrentRoundFunction: TournamentCurrentRoundFunction,
    private val matchRepository: MatchRepository,
) {
    fun process() {
        tournamentRepository.findAll().forEach { league ->
            val todayMatches = tournamentTodayMatchesFunction.execute(league.id) ?: return@forEach
            todayMatches.forEach(this::playMatch)
            val round =
                tournamentCurrentRoundFunction.execute(league.id)
                    ?: throw IllegalStateException("Тур должен быть когда играются его матчи")
            if (round.matches.all{ matchRepository.get(it).passed() }) {
                round.passed = true
            }
        }
    }

    private fun playMatch(match: Match) {
        val homeScored = Random.nextInt(0, 4)
        val awayScored = Random.nextInt(0, 4)
        val (homeStatus, awayStatus) = if (homeScored == awayScored) {
            MatchTeamStatus.DRAW to MatchTeamStatus.DRAW
        } else if (homeScored > awayScored) {
            MatchTeamStatus.WINNER to MatchTeamStatus.LOSER
        } else {
            MatchTeamStatus.LOSER to MatchTeamStatus.WINNER
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