package com.footballmanager.functions

import com.footballmanager.application.repository.TournamentRepository
import com.footballmanager.entities.League
import com.footballmanager.entities.season.Season
import com.footballmanager.seasons.SeasonService
import org.springframework.stereotype.Component
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Component
class TournamentCurrentSeasonFunction(
    private val tournamentRepository: TournamentRepository,
    private val seasonService: SeasonService,
) {
    fun execute(tournament: UUID): Season {
        return tournamentRepository.get(tournament).seasons.map { season -> seasonService.getSeason(season) }.maxBy { it.year }
    }
}