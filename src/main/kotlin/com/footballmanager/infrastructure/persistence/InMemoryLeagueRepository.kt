package com.footballmanager.infrastructure.persistence

import com.footballmanager.domain.model.League
import com.footballmanager.domain.repository.LeagueRepository
import org.springframework.stereotype.Repository
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Repository
class InMemoryLeagueRepository : LeagueRepository {
    private val store = ConcurrentHashMap<UUID, League>()

    override fun findById(id: UUID): League? = store[id]

    override fun findAll(): List<League> = store.values.toList()

    override fun save(league: League) {
        store[league.id] = league
    }
}
