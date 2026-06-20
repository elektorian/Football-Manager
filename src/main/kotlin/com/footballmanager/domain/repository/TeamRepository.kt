package com.footballmanager.domain.repository

import com.footballmanager.domain.model.Team
import java.util.UUID

interface TeamRepository {
    fun findById(id: UUID): Team?
    fun findAll(): List<Team>
    fun save(team: Team)
}
