package com.footballmanager.domain.repository

import com.footballmanager.domain.model.LeagueSchedule
import java.util.UUID

interface ScheduleRepository {
    fun findById(id: UUID): LeagueSchedule?
    fun save(schedule: LeagueSchedule)
}
