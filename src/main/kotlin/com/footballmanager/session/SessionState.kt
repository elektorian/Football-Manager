package com.footballmanager.session

import com.footballmanager.entities.Club
import com.footballmanager.entities.Coach
import com.footballmanager.entities.League
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.UUID

@Component
class SessionState {
    val player: Coach = Coach(
        id = UUID.randomUUID(),
        firstName = "Ivan",
        lastName = "Ivanov",
        birthDate = LocalDate.of(1950, 1, 1),
    )

    @Volatile
    var club: Club? = null
}