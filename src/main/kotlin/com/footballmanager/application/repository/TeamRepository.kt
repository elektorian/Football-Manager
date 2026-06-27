package com.footballmanager.application.repository

import com.footballmanager.entities.Team
import java.util.UUID

interface TeamRepository {
    fun get(id: UUID): Team
    fun findAll(): List<Team>
}