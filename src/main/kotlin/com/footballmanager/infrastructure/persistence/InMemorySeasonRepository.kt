package com.footballmanager.infrastructure.persistence

import com.footballmanager.domain.model.Season
import com.footballmanager.domain.repository.SeasonRepository
import org.springframework.stereotype.Repository
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Repository
class InMemorySeasonRepository : SeasonRepository {
    private val store = ConcurrentHashMap<UUID, Season>()

    override fun findById(id: UUID): Season? = store[id]

    override fun findByIds(ids: Collection<UUID>): List<Season> = ids.mapNotNull { store[it] }

    override fun save(season: Season) {
        store[season.id] = season
    }
}
