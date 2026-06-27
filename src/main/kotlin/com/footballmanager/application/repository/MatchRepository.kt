package com.footballmanager.application.repository

import com.footballmanager.entities.match.Match
import java.util.UUID

interface MatchRepository {
    fun get(id: UUID): Match
    fun save(match: Match)
}