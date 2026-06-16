package com.footballmanager.session

import com.footballmanager.entities.Club
import com.footballmanager.entities.Coach
import com.footballmanager.entities.League
import com.footballmanager.seasons.SeasonService
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.random

@Component
@DependsOn("leagues", "seasonsConfiguration", "teams")
class SessionState(
    private val leagues: ConcurrentHashMap<UUID, League>,
    private val seasonService: SeasonService,
    private val teams: ConcurrentHashMap<UUID, Club>,
) {
    val player: Coach = Coach(
        id = UUID.randomUUID(),
        firstName = "Ivan",
        lastName = "Ivanov",
        birthDate = LocalDate.of(1950, 1, 1),
    )

    @Volatile
    var club: Club? = null

    @PostConstruct
    fun init() {
        club = leagues.values
            .random()
            .seasons
            .random()
            .let { seasonService.getSeason(it) }
            .clubs
            .random()
            .let { teams[it]!! }
    }
}