package com.footballmanager.model.tournament.repository

import com.footballmanager.model.club.entity.Club
import com.footballmanager.model.match.entity.Match
import com.footballmanager.model.tournament.entity.Tournament
import org.springframework.stereotype.Repository
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Repository
class TournamentRepository {
    private val tournaments = ConcurrentHashMap<UUID, Tournament>()

    fun addTournament(tournament: Tournament) {
        tournaments[tournament.id] = tournament
    }

    fun get(id: UUID): Tournament {
        return tournaments[id]!!
    }
}