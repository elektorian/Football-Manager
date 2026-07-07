package com.footballmanager.domain.functions

import com.footballmanager.domain.core.season.model.Season
import com.footballmanager.domain.dao.SeasonRepository
import com.footballmanager.domain.dao.TournamentRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class TournamentCurrentSeasonFunction(
    private val tournamentRepository: TournamentRepository,
    private val seasonRepository: SeasonRepository,
) {
    fun execute(tournament: UUID): Season {
        return tournamentRepository.get(tournament).seasons.map { season -> seasonRepository.get(season) }.maxBy { it.year }
    }
}