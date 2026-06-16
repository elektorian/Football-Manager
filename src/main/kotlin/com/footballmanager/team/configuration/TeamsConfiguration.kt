package com.footballmanager.team.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.footballmanager.entities.Club
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
    fun teams(): ConcurrentHashMap<UUID, Club> {
        val allClubs = ConcurrentHashMap<UUID, Club>()
        for (resource in resolver.getResources("classpath:data/teams/*.json")) {
            val clubs: List<Club> = mapper.readValue(resource.inputStream)
            clubs.forEach { allClubs[it.id] = it }
        }
        return allClubs
    }
}