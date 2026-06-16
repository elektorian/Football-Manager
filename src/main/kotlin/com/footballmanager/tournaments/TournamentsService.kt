package com.footballmanager.tournaments

import com.footballmanager.entities.Club
import com.footballmanager.entities.League
import com.footballmanager.entities.season.Season
import com.footballmanager.entities.match.MatchTeamStatus
import com.footballmanager.matches.MatchesService
import com.footballmanager.seasons.SeasonService
import com.footballmanager.team.TeamService
import com.footballmanager.tournaments.dto.LeagueTeamInfo
import org.springframework.stereotype.Service
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Service
class TournamentsService(
    private val leagues: ConcurrentHashMap<UUID, League>,
    private val seasonService: SeasonService,
    private val matchesService: MatchesService,
    private val teamService: TeamService,
) {
    fun getLeagueTable(leagueId: UUID, seasonId: UUID?): Collection<LeagueTeamInfo> {
        val league = leagues[leagueId] ?: throw IllegalStateException("League not found")
        val seasons = league.seasons.let { seasonService.getSeasons(it) }
        val season = seasons
            .find { it.id == seasonId }
            ?: seasons.maxByOrNull { it.year }
            ?: throw IllegalStateException("Season not found")
        return season.clubs
            .map { teamService.getTeam(it) }
            .map { club -> formTeamInfo(club, season) }
            .sortedByDescending { it.points }
            .mapIndexed { index, teamInfo -> teamInfo.copy(position = index + 1) }
    }

    private fun formTeamInfo(club: Club, season: Season): LeagueTeamInfo {
        val matches = season.matches
            .map { matchesService.getMatch(it) }
            .filter { club.isParticipant(it) }
            .filter { it.passed() }
            .map { it.getResult(club) }
        val victories = matches.count { it.status == MatchTeamStatus.WINNER }
        val draws = matches.count { it.status == MatchTeamStatus.DRAW }
        val losses = matches.count { it.status == MatchTeamStatus.LOSER }
        return LeagueTeamInfo(
            name = club.name,
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