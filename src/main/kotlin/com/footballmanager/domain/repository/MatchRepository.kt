package com.footballmanager.domain.repository

import com.footballmanager.domain.model.Match
import java.util.UUID

interface MatchRepository {
    fun findById(id: UUID): Match?
    fun save(match: Match)
}
