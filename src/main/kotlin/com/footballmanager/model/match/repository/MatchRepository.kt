package com.footballmanager.model.match.repository

import com.footballmanager.model.match.entity.Match
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Repository
class MatchRepository {
    private val matches = ConcurrentHashMap<UUID, Match>()

    fun addMatch(match: Match) {
        matches[match.id] = match
    }

    fun getMatches(date: LocalDate): List<Match> {
        return matches.values.filter { it.date == date }
    }

    fun get(id: UUID): Match {
        return matches[id]!!
    }
}