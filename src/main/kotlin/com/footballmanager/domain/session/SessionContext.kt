package com.footballmanager.domain.session

import com.footballmanager.domain.core.person.model.Person
import com.footballmanager.domain.core.person.model.PersonContract
import com.footballmanager.domain.core.team.model.Team
import com.footballmanager.domain.dao.SeasonRepository
import com.footballmanager.domain.dao.TeamRepository
import com.footballmanager.domain.dao.TournamentRepository
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Component
import java.math.BigDecimal
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
    @Volatile
    var avatar: Person? = null

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
        avatar = Person(
            id = UUID.randomUUID(),
            firstName = "Ivan",
            lastName = "Ivanov",
            birthDate = LocalDate.of(1950, 1, 1),
            contract = PersonContract(
                id = UUID.randomUUID(),
                team = team!!.id,
                startDate = LocalDate.of(2020, 1, 1),
                expiryDate = LocalDate.of(2025, 1, 1),
                salary = BigDecimal.TEN
            )
        )
    }
}