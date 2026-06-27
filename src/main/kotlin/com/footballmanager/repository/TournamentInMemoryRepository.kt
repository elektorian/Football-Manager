package com.footballmanager.repository

import com.footballmanager.application.repository.TournamentRepository
import com.footballmanager.entities.League
import org.springframework.stereotype.Repository
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Repository
class TournamentInMemoryRepository(
    private val tournaments: ConcurrentHashMap<UUID, League>,
) : TournamentRepository {
    override fun get(id: UUID): League {
        return tournaments[id]!!
    }

    override fun findAll(): List<League> {
        return tournaments.values.toList()
    }
}