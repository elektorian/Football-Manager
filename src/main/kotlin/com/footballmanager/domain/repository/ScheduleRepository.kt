package com.footballmanager.domain.repository

import com.footballmanager.entities.season.schedule.LeagueSchedule
import java.util.UUID

interface ScheduleRepository {
    fun get(id: UUID): LeagueSchedule
    fun save(schedule: LeagueSchedule)
}