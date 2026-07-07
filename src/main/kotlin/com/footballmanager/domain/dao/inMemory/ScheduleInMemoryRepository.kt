package com.footballmanager.domain.dao.inMemory

import com.footballmanager.domain.core.schedule.model.LeagueSchedule
import com.footballmanager.domain.dao.ScheduleRepository
import org.springframework.stereotype.Repository
import java.util.*
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