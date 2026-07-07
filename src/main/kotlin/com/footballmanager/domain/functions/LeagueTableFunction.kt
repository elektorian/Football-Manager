package com.footballmanager.domain.functions

import com.footballmanager.domain.core.match.enumeration.MatchTeamStatus
import com.footballmanager.domain.core.season.model.Season
import com.footballmanager.domain.core.team.model.Team
import com.footballmanager.domain.dao.MatchRepository
import com.footballmanager.domain.dao.SeasonRepository
import com.footballmanager.domain.dao.TeamRepository
import com.footballmanager.domain.dao.TournamentRepository
import com.footballmanager.representation.domain.tournament.dto.TournamentTeamInfo
import org.springframework.stereotype.Component
import java.util.*

@Component
class LeagueTableFunction(
    private val tournamentRepository: TournamentRepository,
    private val matchRepository: MatchRepository,
    private val teamRepository: TeamRepository,
    private val seasonRepository: SeasonRepository,
) {
    fun getLeagueTable(leagueId: UUID, seasonId: UUID?): Collection<TournamentTeamInfo> {
        val league = tournamentRepository.get(leagueId)
        val seasons = league.seasons.let { seasonRepository.find(it) }
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

    private fun formTeamInfo(team: Team, season: Season): TournamentTeamInfo {
        val matches = season.matches
            .map { matchRepository.get(it) }
            .filter { team.isParticipant(it) }
            .filter { it.passed() }
            .map { it.getResult(team) }
        val victories = matches.count { it.status == MatchTeamStatus.WINNER }
        val draws = matches.count { it.status == MatchTeamStatus.DRAW }
        val losses = matches.count { it.status == MatchTeamStatus.LOSER }
        return TournamentTeamInfo(
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