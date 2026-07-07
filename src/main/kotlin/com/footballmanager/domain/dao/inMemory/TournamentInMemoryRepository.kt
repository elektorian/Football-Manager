package com.footballmanager.domain.dao.inMemory

import com.footballmanager.domain.core.tournament.model.League
import com.footballmanager.domain.dao.TournamentRepository
import org.springframework.stereotype.Repository
import java.util.*
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