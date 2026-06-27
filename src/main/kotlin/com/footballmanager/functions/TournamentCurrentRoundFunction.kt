package com.footballmanager.functions

import com.footballmanager.application.repository.RoundRepository
import com.footballmanager.entities.season.schedule.Round
import com.footballmanager.seasons.ScheduleService
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class TournamentCurrentRoundFunction(
    private val scheduleService: ScheduleService,
    private val roundRepository: RoundRepository,
    private val tournamentCurrentSeasonFunction: TournamentCurrentSeasonFunction,
) {
    fun execute(tournamentId: UUID): Round? {
        val currentSeason = tournamentCurrentSeasonFunction.execute(tournamentId)
        val schedule = currentSeason.schedule?.let { scheduleService.getSchedule(it) } ?: return null
        return schedule.rounds.map { roundRepository.get(it) }.sortedBy { it.number }.find { !it.passed }
    }
}