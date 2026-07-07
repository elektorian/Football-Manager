package com.footballmanager.domain.dao.inMemory

import com.footballmanager.domain.core.season.model.Season
import com.footballmanager.domain.dao.SeasonRepository
import org.springframework.stereotype.Repository
import java.util.*
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