package com.footballmanager.matches

import com.footballmanager.entities.match.Match
import org.springframework.stereotype.Service
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Service
class MatchesService {
    private val matches = ConcurrentHashMap<UUID, Match>()

    fun getMatch(id: UUID): Match = matches[id]!!

    fun create(match: Match) {
        matches[match.id] = match
    }
}