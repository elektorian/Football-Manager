package com.footballmanager.mock

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.footballmanager.entities.Club
import com.footballmanager.entities.League
import com.footballmanager.entities.Season
import com.footballmanager.session.SessionState
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArraySet

@Configuration
class DataStore(
    private val mapper: ObjectMapper,
    private val sessionState: SessionState,
    private val leagues: Map<UUID, League>,
) {
    @PostConstruct
    fun init() {
        val clubs: CopyOnWriteArraySet<Club> = mapper.readValue(ClassPathResource("data/club.json").inputStream)
        val season = Season(
            id = UUID.randomUUID(),
            year = 2020,
            matches = CopyOnWriteArraySet(),
            league = leagues.values.single(),
            clubs = clubs
        )
        clubs.forEach { it.leagueSeason = season }
        sessionState.club = clubs.random()
    }
}
