package com.footballmanager.domain.repository

import com.footballmanager.entities.League
import java.util.UUID

interface TournamentRepository {
    fun get(id: UUID): League
    fun findAll(): List<League>
}