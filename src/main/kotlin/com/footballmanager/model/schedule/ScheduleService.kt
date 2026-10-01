package com.footballmanager.model.schedule

import com.footballmanager.model.schedule.entity.Schedule
import com.footballmanager.model.schedule.repository.ScheduleRepository
import com.footballmanager.model.season.entity.Season
import org.springframework.stereotype.Service

@Service
class ScheduleService(
    private val leagueScheduleGenerator: LeagueScheduleGenerator,
) {
}