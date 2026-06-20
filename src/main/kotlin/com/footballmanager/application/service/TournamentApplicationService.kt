package com.footballmanager.application.service

import com.footballmanager.application.dto.LeagueTeamInfoDto
import com.footballmanager.application.dto.TournamentDto
import com.footballmanager.application.port.input.TournamentUseCase
import com.footballmanager.domain.repository.LeagueRepository
import com.footballmanager.domain.repository.MatchRepository
import com.footballmanager.domain.repository.SeasonRepository
import com.footballmanager.domain.repository.TeamRepository
import com.footballmanager.domain.service.LeagueTableCalculator
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class TournamentApplicationService(
    private val leagueRepository: LeagueRepository,
    private val seasonRepository: SeasonRepository,
    private val matchRepository: MatchRepository,
    private val teamRepository: TeamRepository,
    private val leagueTableCalculator: LeagueTableCalculator,
) : TournamentUseCase {

    override fun getLeagueTable(leagueId: UUID, seasonId: UUID?): Collection<LeagueTeamInfoDto> {
        val league = leagueRepository.findById(leagueId) ?: throw IllegalStateException("League not found")
        val seasons = seasonRepository.findByIds(league.seasons.toList())
        val season = if (seasonId != null) {
            seasons.find { it.id == seasonId } ?: throw IllegalStateException("Season not found")
        } else {
            seasons.maxByOrNull { it.year } ?: throw IllegalStateException("No seasons found")
        }
        val allTeams = teamRepository.findAll()
        val table = leagueTableCalculator.calculate(
            teamIds = season.teams,
            teamNames = allTeams.associate { it.id to it.name },
            matches = season.matches.mapNotNull { matchRepository.findById(it) },
        )
        return table.map { entry ->
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
    }

    override fun getTournament(id: UUID): TournamentDto {
        val league = leagueRepository.findById(id) ?: throw IllegalStateException("Tournament not found")
        return TournamentDto(id = league.id, name = league.name)
    }
}
