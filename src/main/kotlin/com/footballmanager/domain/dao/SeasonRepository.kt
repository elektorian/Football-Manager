package com.footballmanager.domain.dao

import com.footballmanager.domain.core.season.model.Season
import java.util.*

interface SeasonRepository {
    fun get(id: UUID): Season
    fun find(ids: Collection<UUID>): Collection<Season>
    fun save(season: Season)
}