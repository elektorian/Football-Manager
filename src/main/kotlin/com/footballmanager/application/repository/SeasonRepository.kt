package com.footballmanager.application.repository

import com.footballmanager.entities.season.Season
import java.util.UUID

interface SeasonRepository {
    fun get(id: UUID): Season
    fun find(ids: Collection<UUID>): Collection<Season>
    fun save(season: Season)
}