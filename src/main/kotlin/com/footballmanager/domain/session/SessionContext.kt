package com.footballmanager.domain.session

import com.footballmanager.domain.core.person.model.Coach
import com.footballmanager.domain.core.team.model.Team
import com.footballmanager.domain.dao.SeasonRepository
import com.footballmanager.domain.dao.TeamRepository
import com.footballmanager.domain.dao.TournamentRepository
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.*

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