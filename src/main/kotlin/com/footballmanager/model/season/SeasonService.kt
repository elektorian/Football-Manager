package com.footballmanager.model.season

import com.footballmanager.model.schedule.generator.LeagueScheduleGenerator
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
        tournament: UUID,
    ): Season {
        val seasonId = UUID.randomUUID()
        val schedule = leagueScheduleGenerator.generateLeagueSchedule(
            year = year,
            clubs = clubs,
            seasonId = seasonId,
        )
        val season = Season(
            id = seasonId,
            schedule = schedule.id,
            year = year,
            clubs = clubs,
            tournament = tournament,
        )
        seasonRepository.addSeason(season)
        return season
    }
}