package com.footballmanager.session

import com.footballmanager.entities.Club
import com.footballmanager.entities.Coach
import com.footballmanager.entities.League
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Component
class SessionState(
    private val leagues: ConcurrentHashMap<UUID, League>,
) {
    val player: Coach = Coach(
        id = UUID.randomUUID(),
        firstName = "Ivan",
        lastName = "Ivanov",
        birthDate = LocalDate.of(1950, 1, 1),
    )

    @Volatile
    var club: Club? = leagues.values.random().seasons.random().clubs.random()
}