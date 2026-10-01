package com.footballmanager.model.tournament

import com.footballmanager.model.tournament.entity.Tournament
import com.footballmanager.model.tournament.repository.TournamentRepository
import org.springframework.stereotype.Service
import java.util.UUID
import java.util.concurrent.CopyOnWriteArrayList

@Service
class TournamentService(
    private val tournamentRepository: TournamentRepository,
) {
    fun createTournament(
        id: UUID,
        name: String,
        size: Int,
    ): Tournament {
        val tournament = Tournament(
            id = id,
            name = name,
            size = size,
            seasons = CopyOnWriteArrayList()
        )
        tournamentRepository.addTournament(tournament)
        return tournament
    }
}