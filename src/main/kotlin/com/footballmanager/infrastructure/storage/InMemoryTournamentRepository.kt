package com.footballmanager.infrastructure.storage

import com.footballmanager.domain.tournament.Tournament
import com.footballmanager.domain.repository.TournamentRepository
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class InMemoryTournamentRepository : TournamentRepository {
    private val store = ConcurrentHashMap<UUID, Tournament>()

    override fun save(tournament: Tournament): Tournament {
        store[tournament.id] = tournament
        return tournament
    }

    override fun get(id: UUID): Tournament {
        return store[id]!!
    }
}
