package com.footballmanager.application.service

import com.footballmanager.application.dto.CoachDto
import com.footballmanager.application.dto.LeagueInfoDto
import com.footballmanager.application.dto.LeagueTeamInfoDto
import com.footballmanager.application.dto.MatchInfoDto
import com.footballmanager.application.dto.RoundInfoDto
import com.footballmanager.application.dto.TeamInfoDto
import com.footballmanager.application.port.input.ProfileUseCase
import com.footballmanager.domain.model.Coach
import com.footballmanager.domain.model.Team
import com.footballmanager.domain.repository.LeagueRepository
import com.footballmanager.domain.repository.MatchRepository
import com.footballmanager.domain.repository.RoundRepository
import com.footballmanager.domain.repository.ScheduleRepository
import com.footballmanager.domain.repository.SeasonRepository
import com.footballmanager.domain.repository.TeamRepository
import com.footballmanager.domain.service.LeagueTableCalculator
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class ProfileApplicationService(
    private val teamRepository: TeamRepository,
    private val leagueRepository: LeagueRepository,
    private val seasonRepository: SeasonRepository,
    private val matchRepository: MatchRepository,
    private val roundRepository: RoundRepository,
    private val scheduleRepository: ScheduleRepository,
    private val leagueTableCalculator: LeagueTableCalculator,
) : ProfileUseCase {

    private var currentCoach: Coach = Coach(
        id = UUID.randomUUID(),
        firstName = "Ivan",
        lastName = "Ivanov",
        birthDate = java.time.LocalDate.of(1950, 1, 1),
    )
    private var currentTeam: Team? = null

    fun setCurrentTeam(team: Team) {
        currentTeam = team
    }

    override fun getCoach(): CoachDto = CoachDto(
        id = currentCoach.id,
        firstName = currentCoach.firstName,
        lastName = currentCoach.lastName,
        birthDate = currentCoach.birthDate,
    )

    override fun getLeague(): LeagueInfoDto? {
        val team = currentTeam ?: return null
        val leagueId = team.tournaments["LEAGUE"] ?: return null
        val league = leagueRepository.findById(leagueId) ?: return null
        val seasons = seasonRepository.findByIds(league.seasons.toList())
        val season = seasons.maxByOrNull { it.year } ?: return null
        val allTeams = teamRepository.findAll()
        val table = leagueTableCalculator.calculate(
            teamIds = season.teams,
            teamNames = allTeams.associate { it.id to it.name },
            matches = season.matches.mapNotNull { matchRepository.findById(it) },
        )
        val leagueTableDtos = table.map { entry ->
            LeagueTeamInfoDto(
                teamId = entry.teamId,
                name = entry.name,
                victories = entry.victories,
                draws = entry.draws,
                losses = entry.losses,
                goalsScored = entry.goalsScored,
                goalsConceded = entry.goalsConceded,
                position = entry.position,
                points = entry.points,
            )
        }
        val roundDtos = season.schedule?.let { sid ->
            val schedule = scheduleRepository.findById(sid) ?: return@let null
            schedule.rounds.mapNotNull { rid ->
                val round = roundRepository.findById(rid) ?: return@mapNotNull null
                val teamsByName = allTeams.associateBy { it.id }
                RoundInfoDto(
                    number = round.number,
                    passed = round.passed,
                    matches = round.matches.mapNotNull { mid ->
                        val match = matchRepository.findById(mid) ?: return@mapNotNull null
                        MatchInfoDto(
                            id = match.id,
                            date = match.date,
                            homeTeamId = match.homeTeam,
                            homeTeamName = teamsByName[match.homeTeam]?.name ?: "",
                            homeTeamScore = match.homeTeamResult?.scored?.toString() ?: "-",
                            awayTeamId = match.awayTeam,
                            awayTeamName = teamsByName[match.awayTeam]?.name ?: "",
                            awayTeamScore = match.awayTeamResult?.scored?.toString() ?: "-",
                        )
                    },
                )
            }
        }
        return LeagueInfoDto(
            leagueId = league.id,
            leagueName = league.name,
            table = leagueTableDtos,
            rounds = roundDtos,
        )
    }

    override fun getTeam(): TeamInfoDto? {
        val team = currentTeam ?: return null
        return TeamInfoDto(
            id = team.id,
            name = team.name,
            abbreviation = team.abbreviation,
            city = team.city,
            country = team.country,
        )
    }
}
