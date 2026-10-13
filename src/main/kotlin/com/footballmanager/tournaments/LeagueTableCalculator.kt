package com.footballmanager.tournaments

import com.footballmanager.model.club.repository.ClubRepository
import com.footballmanager.model.common.CommonRepository
import com.footballmanager.model.match.repository.MatchRepository
import com.footballmanager.model.schedule.repository.RoundRepository
import com.footballmanager.model.schedule.repository.ScheduleRepository
import com.footballmanager.model.tournament.repository.TournamentRepository
import com.footballmanager.tournaments.model.LeagueInfo
import com.footballmanager.tournaments.model.LeagueTeamInfo
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class LeagueTableCalculator(
    private val tournamentRepository: TournamentRepository,
    private val commonRepository: CommonRepository,
    private val clubRepository: ClubRepository,
    private val scheduleRepository: ScheduleRepository,
    private val roundRepository: RoundRepository,
    private val matchRepository: MatchRepository,
) {
    fun getLeagueInfo(
        leagueId: UUID,
        year: Int? = null,
    ): LeagueInfo {
        val league = tournamentRepository.get(leagueId)
        val season = commonRepository.getSeasonByLeague(leagueId, year)
        val schedule = scheduleRepository.get(season.schedule)
        val rounds = schedule.rounds.map { roundRepository.get(it) }
        val matches = rounds.flatMap { round -> round.matches.map { matchRepository.get(it) } }
        val clubs = season.clubs.map { clubRepository.getClub(it) }
        var teams = clubs.map { club ->
            val teamMatches = matches.filter { it.isParticipant(club.id) }.filter { it.result != null }
            val goalsScored = teamMatches.sumOf { it.result!!.getScored(club.id) }
            val goalsConceded = teamMatches.sumOf { it.result!!.getConceded(club.id) }
            val wins = teamMatches.filter { it.result!!.isWinner(club.id) }.size
            val draws = teamMatches.filter { it.result!!.isDraw() }.size
            LeagueTeamInfo(
                teamId = club.id,
                name = club.name,
                position = 0,
                matches = teamMatches.size,
                wins = wins,
                draws = draws,
                losses = teamMatches.filter { it.result!!.isLoser(club.id) }.size,
                scored = goalsScored,
                conceded = goalsConceded,
                diff = goalsScored - goalsConceded,
                points = wins * 3 + draws,
            )
        }
        teams = teams.sortedWith(
            compareByDescending <LeagueTeamInfo> { it.points }
                .thenByDescending { it.diff }
                .thenByDescending { it.scored }
                .thenByDescending { it.wins }
                .thenBy { it.name }
        )
        teams = teams.mapIndexed { i, team -> team.copy(position = i + 1) }
        return LeagueInfo(
            leagueId = leagueId,
            name = league.name,
            teams = teams,
        )
    }
}
