package com.footballmanager.functions

import com.footballmanager.domain.repository.MatchRepository
import com.footballmanager.calendar.CurrentMomentHolder
import com.footballmanager.entities.match.Match
import org.springframework.stereotype.Component
import java.util.*

@Component
class TournamentTodayMatchesFunction(
    private val matchRepository: MatchRepository,
    private val currentMomentHolder: CurrentMomentHolder,
    private val tournamentCurrentRoundFunction: TournamentCurrentRoundFunction,
) {
    fun execute(tournamentId: UUID): List<Match>? {
        val currentRound = tournamentCurrentRoundFunction.execute(tournamentId) ?: return null
        val currentDate = currentMomentHolder.get().toLocalDate()
        val todayMatches = currentRound.matches
            .map { matchRepository.get(it) }
            .filter { it.date == currentDate }
        return todayMatches
    }
}