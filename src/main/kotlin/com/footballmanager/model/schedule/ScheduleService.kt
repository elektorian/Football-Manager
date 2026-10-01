package com.footballmanager.model.schedule

import com.footballmanager.model.schedule.entity.Schedule
import com.footballmanager.model.schedule.repository.ScheduleRepository
import com.footballmanager.model.season.entity.Season
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ScheduleService(
    private val scheduleRepository: ScheduleRepository
) {
    fun createSchedule(
        id: UUID,
        rounds: List<UUID>,
    ) : Schedule {
        val schedule = Schedule(
            id = id,
            rounds = rounds,
        )
        scheduleRepository.createSchedule(schedule)
        return schedule
    }
}