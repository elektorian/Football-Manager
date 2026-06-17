package com.footballmanager.functions

import com.footballmanager.entities.League
import com.footballmanager.entities.season.Season
import com.footballmanager.seasons.SeasonService
import org.springframework.stereotype.Component
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Component
class TournamentCurrentSeasonFunction(
    private val leagues: ConcurrentHashMap<UUID, League>,
    private val seasonService: SeasonService,
) {
    fun execute(tournament: UUID): Season {
        return leagues[tournament]!!.seasons.map { season -> seasonService.getSeason(season) }.maxBy { it.year }
    }
}