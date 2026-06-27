package com.footballmanager.application.repository

import com.footballmanager.domain.repository.ScheduleRepository
import com.footballmanager.entities.season.schedule.LeagueSchedule
import org.springframework.stereotype.Repository
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Repository
class ScheduleInMemoryRepository : ScheduleRepository {
    private val schedules = ConcurrentHashMap<UUID, LeagueSchedule>()

    override fun get(id: UUID): LeagueSchedule {
        return schedules[id]!!
    }

    override fun save(schedule: LeagueSchedule) {
        schedules[schedule.id] = schedule
    }
}