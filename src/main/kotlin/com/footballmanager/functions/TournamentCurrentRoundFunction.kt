package com.footballmanager.functions

import com.footballmanager.domain.repository.RoundRepository
import com.footballmanager.domain.repository.ScheduleRepository
import com.footballmanager.entities.season.schedule.Round
import org.springframework.stereotype.Component
import java.util.UUID

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