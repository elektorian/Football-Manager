package com.footballmanager.domain.dao

import com.footballmanager.domain.core.tournament.model.League
import java.util.*

interface TournamentRepository {
    fun get(id: UUID): League
    fun findAll(): List<League>
}