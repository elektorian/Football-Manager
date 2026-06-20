package com.footballmanager.infrastructure.persistence

import com.footballmanager.domain.model.LeagueSchedule
import com.footballmanager.domain.repository.ScheduleRepository
import org.springframework.stereotype.Repository
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Repository
class InMemoryScheduleRepository : ScheduleRepository {
    private val store = ConcurrentHashMap<UUID, LeagueSchedule>()

    override fun findById(id: UUID): LeagueSchedule? = store[id]

    override fun save(schedule: LeagueSchedule) {
        store[schedule.id] = schedule
    }
}
