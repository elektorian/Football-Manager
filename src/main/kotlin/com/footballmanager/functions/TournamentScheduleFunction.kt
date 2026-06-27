package com.footballmanager.functions

import com.footballmanager.application.repository.RoundRepository
import com.footballmanager.application.repository.ScheduleRepository
import com.footballmanager.entities.season.schedule.Round
import com.footballmanager.seasons.ScheduleService
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class TournamentScheduleFunction(
    private val scheduleRepository: ScheduleRepository,
    private val roundRepository: RoundRepository,
    private val tournamentCurrentSeasonFunction: TournamentCurrentSeasonFunction,
) {
    fun execute(tournamentId: UUID): List<Round>? {
        val season = tournamentCurrentSeasonFunction.execute(tournamentId)
        val schedule = season.schedule?.let { scheduleRepository.get(it) } ?: return null
        return schedule.rounds.map { roundRepository.get(it) }
    }
}
