package com.footballmanager.model.season

import com.footballmanager.model.schedule.LeagueScheduleGenerator
import com.footballmanager.model.schedule.repository.ScheduleRepository
import com.footballmanager.model.season.entity.Season
import com.footballmanager.model.season.repository.SeasonRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class SeasonService(
    private val seasonRepository: SeasonRepository,
    private val leagueScheduleGenerator: LeagueScheduleGenerator,
) {
    fun createSeason(
        year: Int,
        clubs: List<UUID>,
    ): Season {
        val schedule = leagueScheduleGenerator.generateLeagueSchedule(
            year = year,
            clubs = clubs,
        )
        val season = Season(
            id = UUID.randomUUID(),
            schedule = schedule.id,
            year = year,
            clubs = clubs,
        )
        seasonRepository.addSeason(season)
        return season
    }
}