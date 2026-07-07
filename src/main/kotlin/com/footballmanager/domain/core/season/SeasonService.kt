package com.footballmanager.domain.core.season

import com.footballmanager.domain.core.schedule.ScheduleService
import com.footballmanager.domain.core.season.model.Season
import com.footballmanager.domain.core.team.model.Team
import com.footballmanager.domain.core.tournament.enumerations.TournamentType
import com.footballmanager.domain.core.tournament.model.League
import com.footballmanager.domain.dao.SeasonRepository
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.CopyOnWriteArraySet

@Service
class SeasonService(
    private val scheduleService: ScheduleService,
    private val seasonRepository: SeasonRepository,
) {
    fun create(
        year: Int,
        league: League,
        teams: CopyOnWriteArraySet<Team>,
    ) {
        val season = Season(
            id = UUID.randomUUID(),
            year = year,
            matches = CopyOnWriteArraySet(),
            league = league.id,
            teams = CopyOnWriteArraySet(teams.map { it.id }),
        )
        season.schedule = scheduleService.generateLeagueSchedule(season).id
        league.seasons.add(season.id)
        teams.forEach { it.tournaments[TournamentType.LEAGUE] = league.id }
        seasonRepository.save(season)
    }
}