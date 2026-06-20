package com.footballmanager.players.configuration

import com.footballmanager.entities.Club
import com.footballmanager.players.PlayerService
import com.footballmanager.players.model.Player
import com.footballmanager.players.model.PlayerContract
import com.footballmanager.utils.Transliterator
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration
import java.time.LocalDate
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.random.Random

@Configuration
class PlayersConfiguration(
    private val teams: ConcurrentHashMap<UUID, Club>,
    private val playerService: PlayerService,
) {
    private companion object {
        private val faker = com.github.javafaker.Faker(Locale("ru"))
    }

    @PostConstruct
    fun setupFirstSeasons() {
        teams.keys.forEach { team ->
            repeat(22) {
                val player = generatePlayer(team)
                playerService.register(player)
            }
        }
    }

    private fun generatePlayer(team: UUID): Player {
        val firstName = Transliterator.transliterate(faker.name().firstName())
        val lastName = Transliterator.transliterate(faker.name().lastName())
        return Player(
            id = UUID.randomUUID(),
            firstName = firstName,
            lastName = lastName,
            nickname = null,
            birthDate = LocalDate.now().minusYears(17L + Random.nextInt(22)),
            contract = PlayerContract(
                id = UUID.randomUUID(),
                startDate = LocalDate.of(2020, 6, 21),
                expiryDate = LocalDate.of(2023, 6, 20),
                team = team,
                salary = Random.nextInt(1000, 40000).toBigDecimal(),
            ),
        )
    }
}
