package com.footballmanager.domain.core.tournament.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.footballmanager.domain.core.tournament.model.League
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Configuration
class TournamentConfiguration(
    private val mapper: ObjectMapper,
    private val resolver: PathMatchingResourcePatternResolver,
) {
    @Bean
    fun leagues(): ConcurrentHashMap<UUID, League> {
        val allLeagues = ConcurrentHashMap<UUID, League>()
        for (resource in resolver.getResources("classpath:data/leagues/*.json")) {
            val league: League = mapper.readValue(resource.inputStream)
            allLeagues[league.id] = league
        }
        return allLeagues
    }
}