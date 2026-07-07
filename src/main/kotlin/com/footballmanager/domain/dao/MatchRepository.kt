package com.footballmanager.domain.dao

import com.footballmanager.domain.core.match.model.Match
import java.util.*

interface MatchRepository {
    fun get(id: UUID): Match
    fun save(match: Match)
}