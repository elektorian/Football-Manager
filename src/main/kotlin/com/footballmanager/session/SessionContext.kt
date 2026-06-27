package com.footballmanager.session

import com.footballmanager.domain.repository.SeasonRepository
import com.footballmanager.domain.repository.TeamRepository
import com.footballmanager.domain.repository.TournamentRepository
import com.footballmanager.entities.Team
import com.footballmanager.entities.Coach
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.*
import kotlin.collections.random

/**
 * НЕ СОДЕРЖИТ БИЗНЕС ЛОГИКИ
 */
@Component
@DependsOn("leagues", "seasonsConfiguration", "teams")
class SessionContext(
    private val tournamentRepository: TournamentRepository,
    private val seasonRepository: SeasonRepository,
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
            .let { seasonRepository.get(it) }
            .teams
            .random()
            .let { teamRepository.get(it) }
    }
}