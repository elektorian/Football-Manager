package com.footballmanager.session

import com.footballmanager.application.repository.TeamRepository
import com.footballmanager.application.repository.TournamentRepository
import com.footballmanager.entities.Team
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

/**
 * НЕ СОДЕРЖИТ БИЗНЕС ЛОГИКИ
 */
@Component
@DependsOn("leagues", "seasonsConfiguration", "teams")
class SessionContext(
    private val tournamentRepository: TournamentRepository,
    private val seasonService: SeasonService,
    private val teamRepository: TeamRepository,
) {
    val player: Coach = Coach(
        id = UUID.randomUUID(),
        firstName = "Ivan",
        lastName = "Ivanov",
        birthDate = LocalDate.of(1950, 1, 1),
    )

    @Volatile
    var team: Team? = null

    @PostConstruct
    fun init() {
        team = tournamentRepository.findAll()
            .random()
            .seasons
            .random()
            .let { seasonService.getSeason(it) }
            .teams
            .random()
            .let { teamRepository.get(it) }
    }
}