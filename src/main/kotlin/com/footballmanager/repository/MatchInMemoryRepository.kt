package com.footballmanager.repository

import com.footballmanager.application.repository.MatchRepository
import com.footballmanager.entities.match.Match
import org.springframework.stereotype.Repository
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Repository
class MatchInMemoryRepository : MatchRepository {
    private val matches = ConcurrentHashMap<UUID, Match>()

    override fun get(id: UUID): Match {
        return matches[id]!!
    }

    override fun save(match: Match) {
        matches[match.id] = match
    }
}