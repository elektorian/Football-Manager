package com.footballmanager.domain.functions

import com.footballmanager.domain.core.calendar.CurrentMomentHolder
import com.footballmanager.domain.core.match.model.Match
import com.footballmanager.domain.dao.MatchRepository
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