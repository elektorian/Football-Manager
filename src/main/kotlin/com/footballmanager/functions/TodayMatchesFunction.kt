package com.footballmanager.functions

import com.footballmanager.calendar.CurrentMomentHolder
import com.footballmanager.entities.match.Match
import com.footballmanager.matches.MatchesService
import com.footballmanager.rounds.RoundsService
import com.footballmanager.seasons.ScheduleService
import com.footballmanager.session.SessionContext
import org.springframework.stereotype.Component
import java.util.*

@Component
class TodayMatchesFunction(
    private val scheduleService: ScheduleService,
    private val roundsService: RoundsService,
    private val matchesService: MatchesService,
    private val tournamentCurrentSeasonFunction: TournamentCurrentSeasonFunction,
    private val currentMomentHolder: CurrentMomentHolder,
) {
    fun execute(tournamentId: UUID): List<Match> {
        val currentSeason = tournamentCurrentSeasonFunction.execute(tournamentId)
        val schedule = currentSeason.schedule?.let { scheduleService.getSchedule(it) } ?: return emptyList()
        val currentRound = schedule.rounds.map { roundsService.getRound(it) }.sortedBy { it.number }.first { !it.passed }
        val currentDate = currentMomentHolder.get().toLocalDate()
        val todayMatches = currentRound.matches
            .map { matchesService.getMatch(it) }
            .filter { it.date == currentDate }
        return todayMatches
    }
}