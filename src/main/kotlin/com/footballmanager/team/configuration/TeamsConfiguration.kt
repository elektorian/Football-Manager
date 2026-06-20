package com.footballmanager.team.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.footballmanager.entities.Team
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Configuration
class TeamsConfiguration(
    private val mapper: ObjectMapper,
    private val resolver: PathMatchingResourcePatternResolver,
) {
    @Bean
    fun teams(): ConcurrentHashMap<UUID, Team> {
        val allTeams = ConcurrentHashMap<UUID, Team>()
        for (resource in resolver.getResources("classpath:data/teams/*.json")) {
            val teams: List<Team> = mapper.readValue(resource.inputStream)
            teams.forEach { allTeams[it.id] = it }
        }
        return allTeams
    }
}