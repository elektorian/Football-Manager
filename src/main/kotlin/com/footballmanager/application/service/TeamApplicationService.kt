package com.footballmanager.application.service

import com.footballmanager.application.dto.MatchInfoDto
import com.footballmanager.application.dto.TeamInfoDto
import com.footballmanager.application.port.input.TeamUseCase
import com.footballmanager.domain.repository.LeagueRepository
import com.footballmanager.domain.repository.MatchRepository
import com.footballmanager.domain.repository.RoundRepository
import com.footballmanager.domain.repository.ScheduleRepository
import com.footballmanager.domain.repository.SeasonRepository
import com.footballmanager.domain.repository.TeamRepository
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class TeamApplicationService(
    private val teamRepository: TeamRepository,
    private val matchRepository: MatchRepository,
    private val roundRepository: RoundRepository,
    private val scheduleRepository: ScheduleRepository,
    private val leagueRepository: LeagueRepository,
    private val seasonRepository: SeasonRepository,
) : TeamUseCase {

    override fun getTeam(id: UUID): TeamInfoDto {
        val team = teamRepository.findById(id) ?: throw IllegalStateException("Team not found")
        return TeamInfoDto(
            id = team.id,
            name = team.name,
            abbreviation = team.abbreviation,
            city = team.city,
            country = team.country,
        )
    }

    override fun getTeamSchedule(teamId: UUID): List<MatchInfoDto> {
        val team = teamRepository.findById(teamId) ?: return emptyList()
        val allTeams = teamRepository.findAll().associateBy { it.id }
        return team.tournaments.values.flatMap { tournamentId ->
            buildTournamentMatches(tournamentId, team, allTeams)
        }.sortedBy { it.date }
    }

    private fun buildTournamentMatches(
        leagueId: UUID,
        team: com.footballmanager.domain.model.Team,
        allTeams: Map<UUID, com.footballmanager.domain.model.Team>,
    ): List<MatchInfoDto> {
        val scheduleId = leagueRepository.findById(leagueId)
            ?.let { league -> seasonRepository.findByIds(league.seasons.toList()) }
            ?.maxByOrNull { it.year }
            ?.schedule
            ?: return emptyList()
        val schedule = scheduleRepository.findById(scheduleId) ?: return emptyList()
        return schedule.rounds
            .mapNotNull { roundRepository.findById(it) }
            .flatMap { it.matches }
            .mapNotNull { matchRepository.findById(it) }
            .filter { team.isParticipant(it) }
            .map { match ->
                MatchInfoDto(
                    id = match.id,
                    date = match.date,
                    homeTeamId = match.homeTeam,
                    homeTeamName = allTeams[match.homeTeam]?.name ?: "",
                    homeTeamScore = match.homeTeamResult?.scored?.toString() ?: "-",
                    awayTeamId = match.awayTeam,
                    awayTeamName = allTeams[match.awayTeam]?.name ?: "",
                    awayTeamScore = match.awayTeamResult?.scored?.toString() ?: "-",
                )
            }
    }
}
