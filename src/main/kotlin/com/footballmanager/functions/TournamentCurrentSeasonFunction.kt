package com.footballmanager.functions

import com.footballmanager.domain.repository.SeasonRepository
import com.footballmanager.domain.repository.TournamentRepository
import com.footballmanager.entities.season.Season
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class TournamentCurrentSeasonFunction(
    private val tournamentRepository: TournamentRepository,
    private val seasonRepository: SeasonRepository,
) {
    fun execute(tournament: UUID): Season {
        return tournamentRepository.get(tournament).seasons.map { season -> seasonRepository.get(season) }.maxBy { it.year }
    }
}