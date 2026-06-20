package com.footballmanager.functions

import com.footballmanager.entities.season.schedule.Round
import com.footballmanager.rounds.RoundsService
import com.footballmanager.seasons.ScheduleService
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class TournamentScheduleFunction(
    private val scheduleService: ScheduleService,
    private val roundsService: RoundsService,
    private val tournamentCurrentSeasonFunction: TournamentCurrentSeasonFunction,
) {
    fun execute(tournamentId: UUID): List<Round>? {
        val season = tournamentCurrentSeasonFunction.execute(tournamentId)
        val schedule = season.schedule?.let { scheduleService.getSchedule(it) } ?: return null
        return schedule.rounds.map { roundsService.getRound(it) }
    }
}
