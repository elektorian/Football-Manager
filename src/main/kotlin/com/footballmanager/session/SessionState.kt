package com.footballmanager.session

import com.footballmanager.entities.Club
import com.footballmanager.entities.Coach
import com.footballmanager.entities.League
import com.footballmanager.seasons.SeasonService
import com.footballmanager.team.TeamService
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Component
class SessionState(
    leagues: ConcurrentHashMap<UUID, League>,
    private val seasonService: SeasonService,
    private val teamService: TeamService,
) {
    val player: Coach = Coach(
        id = UUID.randomUUID(),
        firstName = "Ivan",
        lastName = "Ivanov",
        birthDate = LocalDate.of(1950, 1, 1),
    )

    @Volatile
    var club: Club? = leagues.values
        .random()
        .seasons
        .random()
        .let { seasonService.getSeason(it) }
        .clubs
        .random()
        .let { teamService.getTeam(it) }
}