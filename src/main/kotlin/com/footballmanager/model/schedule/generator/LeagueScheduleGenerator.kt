package com.footballmanager.model.schedule.generator

import com.footballmanager.model.schedule.entity.Schedule
import java.util.UUID

interface LeagueScheduleGenerator {
    fun generateLeagueSchedule(
        clubs: List<UUID>,
        seasonId: UUID,
        year: Int,
    ): Schedule
}