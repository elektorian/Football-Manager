package com.footballmanager.application.repository

import com.footballmanager.domain.repository.SeasonRepository
import com.footballmanager.entities.season.Season
import org.springframework.stereotype.Repository
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Repository
class SeasonInMemoryRepository : SeasonRepository {
    private val seasons = ConcurrentHashMap<UUID, Season>()

    override fun get(id: UUID): Season {
        return seasons[id]!!
    }

    override fun find(ids: Collection<UUID>): Collection<Season> {
        return seasons.values.filter { season -> ids.contains(season.id) }
    }

    override fun save(season: Season) {
        seasons[season.id] = season
    }
}