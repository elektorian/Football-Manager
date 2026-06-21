package com.footballmanager.infrastructure.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.footballmanager.domain.model.League
import com.footballmanager.domain.model.Player
import com.footballmanager.domain.model.PlayerContract
import com.footballmanager.domain.model.Season
import com.footballmanager.domain.model.Team
import com.footballmanager.domain.repository.LeagueRepository
import com.footballmanager.domain.service.ScheduleGenerator
import com.footballmanager.domain.repository.MatchRepository
import com.footballmanager.domain.repository.PlayerRepository
import com.footballmanager.domain.repository.RoundRepository
import com.footballmanager.domain.repository.ScheduleRepository
import com.footballmanager.domain.repository.SeasonRepository
import com.footballmanager.domain.repository.TeamRepository
import com.footballmanager.utils.Transliterator
import jakarta.annotation.PostConstruct
import net.datafaker.Faker
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.Locale
import java.util.UUID
import kotlin.random.Random

@Component
class DataSeeder(
    private val mapper: ObjectMapper,
    private val resolver: org.springframework.core.io.support.PathMatchingResourcePatternResolver,
    private val teamRepository: TeamRepository,
    private val leagueRepository: LeagueRepository,
    private val seasonRepository: SeasonRepository,
    private val matchRepository: MatchRepository,
    private val roundRepository: RoundRepository,
    private val playerRepository: PlayerRepository,
    private val scheduleRepository: ScheduleRepository,
    private val scheduleGenerator: ScheduleGenerator,
    private val profileService: com.footballmanager.application.service.ProfileApplicationService,
) {

    private val faker = Faker(Locale("ru"))

    @PostConstruct
    fun seed() {
        loadTeams()
        loadLeagues()
        createInitialSeasons()
        generatePlayers()
        selectRandomTeam()
    }

    private fun loadTeams() {
        for (resource in resolver.getResources("classpath:data/teams/*.json")) {
            val teams: List<Team> = mapper.readValue(resource.inputStream)
            teams.forEach { teamRepository.save(it) }
        }
    }

    private fun loadLeagues() {
        for (resource in resolver.getResources("classpath:data/leagues/*.json")) {
            val league: League = mapper.readValue(resource.inputStream)
            leagueRepository.save(league)
        }
    }

    private fun createInitialSeasons() {
        for (resource in resolver.getResources("classpath:data/seasons/*.json")) {
            val data: FirstSeasonData = mapper.readValue(resource.inputStream)
            val league = leagueRepository.findById(data.league)
                ?: throw IllegalStateException("League ${data.league} not found")
            val teams = data.teams.mapNotNull { teamRepository.findById(it) }

            val season = Season(
                id = UUID.randomUUID(),
                year = 2020,
                league = league.id,
                teams = teams.map { it.id }.toMutableSet(),
                matches = HashSet(),
            )

            val result = scheduleGenerator.generateLeagueSchedule(season, teams.map { it.id })

            result.matches.forEach { matchRepository.save(it) }
            season.matches.addAll(result.matches.map { it.id })
            result.rounds.forEach { roundRepository.save(it) }

            season.schedule = result.schedule.id
            scheduleRepository.save(result.schedule)
            seasonRepository.save(season)

            league.seasons.add(season.id)
            teams.forEach { team ->
                team.tournaments["LEAGUE"] = league.id
                teamRepository.save(team)
            }
        }
    }

    private fun generatePlayers() {
        for (team in teamRepository.findAll()) {
            repeat(22) {
                val firstName = Transliterator.transliterate(faker.expression("#{Name.male_first_name}"))
                val lastName = Transliterator.transliterate(faker.expression("#{Name.male_last_name}"))
                val player = Player(
                    id = UUID.randomUUID(),
                    firstName = firstName,
                    lastName = lastName,
                    nickname = null,
                    birthDate = LocalDate.now().minusYears(17L + Random.nextInt(22)),
                    contract = PlayerContract(
                        id = UUID.randomUUID(),
                        startDate = LocalDate.of(2020, 6, 21),
                        expiryDate = LocalDate.of(2023, 6, 20),
                        team = team.id,
                        salary = Random.nextInt(1000, 40000).toBigDecimal(),
                    ),
                )
                playerRepository.save(player)
                team.players.add(player.id)
            }
            teamRepository.save(team)
        }
    }

    private fun selectRandomTeam() {
        val allTeams = teamRepository.findAll()
        if (allTeams.isNotEmpty()) {
            profileService.setCurrentTeam(allTeams.random())
        }
    }
}

data class FirstSeasonData(
    val id: UUID,
    val league: UUID,
    val teams: Set<UUID>,
)
