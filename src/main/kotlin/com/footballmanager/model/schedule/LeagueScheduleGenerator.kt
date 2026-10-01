package com.footballmanager.model.schedule

import com.footballmanager.model.schedule.entity.Round
import com.footballmanager.model.schedule.entity.Schedule
import com.footballmanager.model.schedule.repository.RoundRepository
import com.footballmanager.model.schedule.repository.ScheduleRepository
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class LeagueScheduleGenerator(
    private val scheduleRepository: ScheduleRepository,
    private val roundRepository: RoundRepository,
) {
    fun generateLeagueSchedule(
        year: Int,
        clubs: List<UUID>
    ): Schedule {
        require(clubs.isNotEmpty())
        require(clubs.size % 2 == 0)
        for (circle in (1..2)) {

        }
    }

    private fun generateCircle(clubs: List<UUID>): List<Round> {
        val mesh: Array<IntArray> = Array(clubs.size) { IntArray(clubs.size) }
        mesh.forEachIndexed { clubPosition, clubMatches ->
            val possibleMatches =
                clubMatches.filterIndexed { matchPosition, matchValue ->
                    matchValue != 1 && matchPosition != clubPosition
                }
            val matchedPosition = possibleMatches.random()
        }
    }
}