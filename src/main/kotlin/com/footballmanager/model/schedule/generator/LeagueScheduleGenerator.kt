package com.footballmanager.model.schedule.generator

import com.footballmanager.model.schedule.entity.Schedule
import java.util.UUID

interface LeagueScheduleGenerator {
    fun generateLeagueSchedule(
        year: Int,
        clubs: List<UUID>
    ): Schedule
}