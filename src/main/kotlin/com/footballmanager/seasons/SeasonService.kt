package com.footballmanager.seasons

import com.footballmanager.entities.Club
import com.footballmanager.entities.League
import com.footballmanager.entities.season.Season
import org.springframework.stereotype.Service
import java.util.UUID
import java.util.concurrent.CopyOnWriteArraySet

@Service
class SeasonService(
    private val scheduleService: ScheduleService
) {
    fun create(
        year: Int,
        league: League,
        clubs: CopyOnWriteArraySet<Club>,
    ) {
        val season = Season(
            id = UUID.randomUUID(),
            year = year,
            matches = CopyOnWriteArraySet(),
            league = league,
            clubs = clubs,
        )
        season.schedule = scheduleService.generateLeagueSchedule(season)
        league.seasons.add(season)
        clubs.forEach { it.leagueSeason = season }
    }
}