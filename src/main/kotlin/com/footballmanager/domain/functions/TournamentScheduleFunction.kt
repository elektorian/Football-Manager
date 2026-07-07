package com.footballmanager.domain.functions

import com.footballmanager.domain.core.schedule.model.Round
import com.footballmanager.domain.dao.RoundRepository
import com.footballmanager.domain.dao.ScheduleRepository
import org.springframework.stereotype.Component
import java.util.*

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
