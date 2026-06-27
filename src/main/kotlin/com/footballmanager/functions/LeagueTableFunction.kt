package com.footballmanager.functions

import com.footballmanager.application.repository.TeamRepository
import com.footballmanager.application.repository.TournamentRepository
import com.footballmanager.entities.Team
import com.footballmanager.entities.League
import com.footballmanager.entities.match.MatchTeamStatus
import com.footballmanager.entities.season.Season
import com.footballmanager.matches.MatchesService
import com.footballmanager.seasons.SeasonService
import com.footballmanager.tournaments.dto.LeagueTeamInfo
import org.springframework.stereotype.Component
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Component
class LeagueTableFunction(
    private val tournamentRepository: TournamentRepository,
    private val seasonService: SeasonService,
    private val matchesService: MatchesService,
    private val teamRepository: TeamRepository,
) {
    fun getLeagueTable(leagueId: UUID, seasonId: UUID?): Collection<LeagueTeamInfo> {
        val league = tournamentRepository.get(leagueId) ?: throw IllegalStateException("League not found")
        val seasons = league.seasons.let { seasonService.getSeasons(it) }
        val season = seasons
            .find { it.id == seasonId }
            ?: seasons.maxByOrNull { it.year }
            ?: throw IllegalStateException("Season not found")
        return season.teams
            .map { teamRepository.get(it) }
            .map { team -> formTeamInfo(team, season) }
            .sortedByDescending { it.points }
            .mapIndexed { index, teamInfo -> teamInfo.copy(position = index + 1) }
    }

    private fun formTeamInfo(team: Team, season: Season): LeagueTeamInfo {
        val matches = season.matches
            .map { matchesService.getMatch(it) }
            .filter { team.isParticipant(it) }
            .filter { it.passed() }
            .map { it.getResult(team) }
        val victories = matches.count { it.status == MatchTeamStatus.WINNER }
        val draws = matches.count { it.status == MatchTeamStatus.DRAW }
        val losses = matches.count { it.status == MatchTeamStatus.LOSER }
        return LeagueTeamInfo(
            teamId = team.id,
            name = team.name,
            victories = victories,
            draws = draws,
            losses = losses,
            goalsScored = matches.sumOf { it.scored },
            goalsConceded = matches.sumOf { it.conceded },
            position = 0,
            points = victories * 3 + draws,
        )
    }
}