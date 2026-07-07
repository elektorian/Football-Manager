package com.footballmanager.domain.core.person.configuration

import com.footballmanager.domain.core.person.PlayerService
import com.footballmanager.domain.core.person.SalaryCeiler
import com.footballmanager.domain.core.person.model.Person
import com.footballmanager.domain.core.person.model.PersonContract
import com.footballmanager.domain.dao.TeamRepository
import com.footballmanager.utils.Transliterator
import jakarta.annotation.PostConstruct
import net.datafaker.Faker
import org.springframework.context.annotation.Configuration
import java.time.LocalDate
import java.util.*
import kotlin.random.Random

@Configuration
class PlayersConfiguration(
    private val teamRepository: TeamRepository,
    private val playerService: PlayerService,
) {
    private companion object {
        private val faker = Faker(Locale("ru"))
    }

    @PostConstruct
    fun setupFirstSeasons() {
        teamRepository.findAll().forEach { team ->
            repeat(22) {
                val player = generatePlayer(team.id)
                playerService.register(player)
            }
        }
    }

    private fun generatePlayer(team: UUID): Person {
        val firstName = Transliterator.transliterate(faker.expression("#{Name.male_first_name}"))
        val lastName = Transliterator.transliterate(faker.expression("#{Name.male_last_name}"))
        val salary = SalaryCeiler.ceiling(Random.nextInt(1000, 40000).toBigDecimal())
        return Person(
            id = UUID.randomUUID(),
            firstName = firstName,
            lastName = lastName,
            nickname = null,
            birthDate = LocalDate.now().minusYears(17L + Random.nextInt(22)),
            contract = PersonContract(
                id = UUID.randomUUID(),
                startDate = LocalDate.of(2020, 6, 21),
                expiryDate = LocalDate.of(2023, 6, 20),
                team = team,
                salary = salary,
            ),
        )
    }
}
