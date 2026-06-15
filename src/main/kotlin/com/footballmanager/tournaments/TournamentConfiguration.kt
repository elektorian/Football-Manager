package com.footballmanager.tournaments

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.footballmanager.entities.League
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Configuration
class TournamentConfiguration(
    private val mapper: ObjectMapper,
) {
    @Bean
    fun leagues(): ConcurrentHashMap<UUID, League> {
        val league: League = mapper.readValue(ClassPathResource("data/league.json").inputStream)
        return ConcurrentHashMap(mapOf(league.id to league))
    }
}