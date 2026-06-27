package com.footballmanager.matches

import com.footballmanager.application.repository.TournamentRepository
import com.footballmanager.entities.League
import com.footballmanager.entities.match.Match
import com.footballmanager.entities.match.MatchTeamResult
import com.footballmanager.entities.match.MatchTeamStatus
import com.footballmanager.functions.TournamentTodayMatchesFunction
import com.footballmanager.functions.TournamentCurrentRoundFunction
import org.springframework.stereotype.Component
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import kotlin.random.Random

@Component
class MatchesEngine(
    private val tournamentRepository: TournamentRepository,
    private val tournamentTodayMatchesFunction: TournamentTodayMatchesFunction,
    private val tournamentCurrentRoundFunction: TournamentCurrentRoundFunction,
    private val matchesService: MatchesService,
) {
    fun process() {
        tournamentRepository.findAll().forEach { league ->
            val todayMatches = tournamentTodayMatchesFunction.execute(league.id) ?: return@forEach
            todayMatches.forEach(this::playMatch)
            val round =
                tournamentCurrentRoundFunction.execute(league.id)
                    ?: throw IllegalStateException("Тур должен быть когда играются его матчи")
            if (round.matches.all{ matchesService.getMatch(it).passed() }) {
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