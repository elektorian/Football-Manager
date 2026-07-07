package com.footballmanager.domain.dao.inMemory

import com.footballmanager.domain.core.match.model.Match
import com.footballmanager.domain.dao.MatchRepository
import org.springframework.stereotype.Repository
import java.util.*
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