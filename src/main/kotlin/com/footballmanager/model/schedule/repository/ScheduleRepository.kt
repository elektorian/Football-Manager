package com.footballmanager.model.schedule.repository

import com.footballmanager.model.club.entity.Club
import com.footballmanager.model.match.entity.Match
import com.footballmanager.model.schedule.entity.Schedule
import org.springframework.stereotype.Repository
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Repository
class ScheduleRepository {
    private val schedules = ConcurrentHashMap<UUID, Schedule>()

    fun createSchedule(schedule: Schedule) {
        schedules[schedule.id] = schedule
    }
}