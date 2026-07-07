package com.footballmanager.domain.dao

import com.footballmanager.domain.core.team.model.Team
import java.util.*

interface TeamRepository {
    fun get(id: UUID): Team
    fun findAll(): List<Team>
}