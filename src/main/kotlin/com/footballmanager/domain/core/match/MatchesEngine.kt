package com.footballmanager.domain.core.match

import com.footballmanager.domain.core.match.enumeration.MatchTeamStatus
import com.footballmanager.domain.core.match.model.Match
import com.footballmanager.domain.core.match.model.MatchPlayerStatistic
import com.footballmanager.domain.core.match.model.MatchTeamPreview
import com.footballmanager.domain.core.match.model.MatchTeamResult
import com.footballmanager.domain.dao.MatchRepository
import com.footballmanager.domain.dao.TournamentRepository
import com.footballmanager.domain.functions.TournamentCurrentRoundFunction
import com.footballmanager.domain.functions.TournamentTodayMatchesFunction
import org.springframework.stereotype.Component
import java.util.UUID
import kotlin.random.Random

@Component
class MatchesEngine(
    private val tournamentRepository: TournamentRepository,
    private val tournamentTodayMatchesFunction: TournamentTodayMatchesFunction,
    private val tournamentCurrentRoundFunction: TournamentCurrentRoundFunction,
    private val matchRepository: MatchRepository,
    private val matchTeamSquadProvider: MatchTeamSquadProvider,
) {
    fun process() {
        tournamentRepository.findAll().forEach { league ->
            val todayMatches = tournamentTodayMatchesFunction.execute(league.id) ?: return@forEach
            todayMatches.forEach { match ->
                match.homeTeamPreview = matchTeamSquadProvider.prepareSquad(match.homeTeam)
                match.awayTeamPreview = matchTeamSquadProvider.prepareSquad(match.awayTeam)
                playMatch(match)
            }
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
            scored = homeScored,
            conceded = awayScored,
            status = homeStatus,
            playersStatistic = generateTeamPlayersStatistic(match.homeTeamPreview!!, homeScored)
        )
        match.awayTeamResult = MatchTeamResult(
            scored = awayScored,
            conceded = homeScored,
            status = awayStatus,
            playersStatistic = generateTeamPlayersStatistic(match.awayTeamPreview!!, awayScored)
        )
    }

    private fun generateTeamPlayersStatistic(
        preview: MatchTeamPreview,
        scored: Int,
    ): Collection<MatchPlayerStatistic> {
        data class Statistic(
            var goals: Int = 0,
            var assists: Int = 0,
        )
        val played = preview.startPlayers + preview.benchPlayers.take(3)
        val statistic = played.associateWith { Statistic() }
        var goals = scored
        while (goals > 0) {
            val author = played.random()
            statistic[author]!!.goals += 1
            val assistant = played.filterNot { it == author }.random()
            statistic[assistant]!!.assists += 1
            goals--
        }
        return statistic.map { (id, stat) ->
            MatchPlayerStatistic(
                player = id,
                goals = stat.goals,
                assists = stat.assists,
                score = 6.0,
            )
        }
    }
}