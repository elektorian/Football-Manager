package com.footballmanager.model.season.repository

import com.footballmanager.model.club.entity.Club
import com.footballmanager.model.match.entity.Match
import com.footballmanager.model.season.entity.Season
import org.springframework.stereotype.Repository
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Repository
class SeasonRepository {
    private val seasons = ConcurrentHashMap<UUID, Season>()

    fun addSeason(season: Season) {
        seasons[season.id] = season
    }

    fun get(id: UUID): Season {
        return seasons[id]!!
    }
}