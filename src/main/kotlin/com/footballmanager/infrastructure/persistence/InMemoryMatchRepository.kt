package com.footballmanager.infrastructure.persistence

import com.footballmanager.domain.model.Match
import com.footballmanager.domain.repository.MatchRepository
import org.springframework.stereotype.Repository
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Repository
class InMemoryMatchRepository : MatchRepository {
    private val store = ConcurrentHashMap<UUID, Match>()

    override fun findById(id: UUID): Match? = store[id]

    override fun save(match: Match) {
        store[match.id] = match
    }
}
