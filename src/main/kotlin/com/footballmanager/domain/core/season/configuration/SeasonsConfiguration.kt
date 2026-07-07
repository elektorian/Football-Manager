package com.footballmanager.domain.core.season.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.footballmanager.configuration.GlobalParameters
import com.footballmanager.domain.core.season.SeasonService
import com.footballmanager.domain.dao.TeamRepository
import com.footballmanager.domain.dao.TournamentRepository
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import java.util.concurrent.CopyOnWriteArraySet

@Configuration
class SeasonsConfiguration(
    private val mapper: ObjectMapper,
    private val resolver: PathMatchingResourcePatternResolver,
    private val seasonService: SeasonService,
    private val tournamentRepository: TournamentRepository,
    private val teamRepository: TeamRepository,
) {
    @PostConstruct
    fun setupFirstSeasons() {
        for (resource in resolver.getResources("classpath:data/seasons/*.json")) {
            val season: FirstSeasonData = mapper.readValue(resource.inputStream)
            seasonService.create(
                year = GlobalParameters.START_YEAR,
                league = tournamentRepository.get(season.league),
                teams = season.teams.mapTo(CopyOnWriteArraySet()) { teamRepository.get(it) },
            )
        }
    }
}