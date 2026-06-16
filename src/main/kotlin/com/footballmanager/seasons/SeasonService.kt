package com.footballmanager.seasons

import com.footballmanager.entities.Club
import com.footballmanager.entities.League
import com.footballmanager.entities.season.Season
import org.springframework.stereotype.Service
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArraySet

@Service
class SeasonService(
    private val scheduleService: ScheduleService
) {
    private val seasons = ConcurrentHashMap<UUID, Season>()

    fun create(
        year: Int,
        league: League,
        clubs: CopyOnWriteArraySet<Club>,
    ) {
        val season = Season(
            id = UUID.randomUUID(),
            year = year,
            matches = CopyOnWriteArraySet(),
            league = league.id,
            clubs = CopyOnWriteArraySet(clubs.map { it.id }),
        )
        season.schedule = scheduleService.generateLeagueSchedule(season).id
        league.seasons.add(season.id)
        clubs.forEach { it.leagueSeason = season.id }
        seasons[season.id] = season
    }

    fun getSeason(id: UUID) = seasons[id]!!

    fun getSeasons(ids: Collection<UUID>) = ids.map { getSeason(it) }
}