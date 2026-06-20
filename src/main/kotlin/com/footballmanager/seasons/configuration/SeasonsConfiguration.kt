package com.footballmanager.seasons.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.footballmanager.configuration.GlobalParameters
import com.footballmanager.entities.Team
import com.footballmanager.entities.League
import com.footballmanager.seasons.SeasonService
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArraySet

@Configuration
class SeasonsConfiguration(
    private val mapper: ObjectMapper,
    private val resolver: PathMatchingResourcePatternResolver,
    private val seasonService: SeasonService,
    private val leagues: ConcurrentHashMap<UUID, League>,
    private val teams: ConcurrentHashMap<UUID, Team>,
) {
    @PostConstruct
    fun setupFirstSeasons() {
        for (resource in resolver.getResources("classpath:data/seasons/*.json")) {
            val season: FirstSeasonData = mapper.readValue(resource.inputStream)
            seasonService.create(
                year = GlobalParameters.START_YEAR,
                league = leagues[season.league]!!,
                teams = season.teams.mapTo(CopyOnWriteArraySet()) { teams[it]!! },
            )
        }
    }
}