package com.footballmanager.domain.repository

import com.footballmanager.domain.model.Season
import java.util.UUID

interface SeasonRepository {
    fun findById(id: UUID): Season?
    fun findByIds(ids: Collection<UUID>): List<Season>
    fun save(season: Season)
}
