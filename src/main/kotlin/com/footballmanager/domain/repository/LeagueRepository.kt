package com.footballmanager.domain.repository

import com.footballmanager.domain.model.League
import java.util.UUID

interface LeagueRepository {
    fun findById(id: UUID): League?
    fun findAll(): List<League>
    fun save(league: League)
}
