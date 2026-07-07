package com.footballmanager.domain.functions

import com.footballmanager.domain.core.schedule.model.Round
import com.footballmanager.domain.dao.RoundRepository
import com.footballmanager.domain.dao.ScheduleRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class TournamentCurrentRoundFunction(
    private val schedulesRepository: ScheduleRepository,
    private val roundRepository: RoundRepository,
    private val tournamentCurrentSeasonFunction: TournamentCurrentSeasonFunction,
) {
    fun execute(tournamentId: UUID): Round? {
        val currentSeason = tournamentCurrentSeasonFunction.execute(tournamentId)
        val schedule = currentSeason.schedule?.let { schedulesRepository.get(it) } ?: return null
        return schedule.rounds.map { roundRepository.get(it) }.sortedBy { it.number }.find { !it.passed }
    }
}