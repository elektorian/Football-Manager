package com.footballmanager.domain.dao

import com.footballmanager.domain.core.schedule.model.LeagueSchedule
import java.util.*

interface ScheduleRepository {
    fun get(id: UUID): LeagueSchedule
    fun save(schedule: LeagueSchedule)
}