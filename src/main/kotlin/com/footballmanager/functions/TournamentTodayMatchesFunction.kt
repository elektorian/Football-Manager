package com.footballmanager.functions

import com.footballmanager.calendar.CurrentMomentHolder
import com.footballmanager.entities.match.Match
import com.footballmanager.matches.MatchesService
import org.springframework.stereotype.Component
import java.util.*

@Component
class TournamentTodayMatchesFunction(
    private val matchesService: MatchesService,
    private val currentMomentHolder: CurrentMomentHolder,
    private val tournamentCurrentRoundFunction: TournamentCurrentRoundFunction,
) {
    fun execute(tournamentId: UUID): List<Match>? {
        val currentRound = tournamentCurrentRoundFunction.execute(tournamentId) ?: return null
        val currentDate = currentMomentHolder.get().toLocalDate()
        val todayMatches = currentRound.matches
            .map { matchesService.getMatch(it) }
            .filter { it.date == currentDate }
        return todayMatches
    }
}