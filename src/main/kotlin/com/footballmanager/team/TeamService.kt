package com.footballmanager.team

import com.footballmanager.application.repository.TeamRepository
import com.footballmanager.entities.Team
import com.footballmanager.functions.TournamentScheduleFunction
import com.footballmanager.matches.MatchesService
import com.footballmanager.team.dto.TeamInfo
import com.footballmanager.tournaments.dto.MatchInfo
import org.springframework.stereotype.Service
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Service
class TeamService(
    private val teamRepository: TeamRepository,
    private val matchesService: MatchesService,
    private val tournamentScheduleFunction: TournamentScheduleFunction,
) {
    fun getTeamInfo(id: UUID): TeamInfo {
        val team = teamRepository.get(id)
        return TeamInfo(
            id = team.id,
            name = team.name,
            abbreviation = team.abbreviation,
            city = team.city,
            country = team.country,
        )
    }

    fun getTeamSchedule(teamId: UUID): List<MatchInfo> {
        val team = teamRepository.get(teamId) ?: return emptyList()

        return team.tournaments.values.flatMap { tournamentId ->
            tournamentScheduleFunction.execute(tournamentId)
                ?.flatMap { round -> round.matches.map { matchesService.getMatch(it) } }
                ?.filter { team.isParticipant(it) }
                ?.map { match ->
                    MatchInfo(
                        id = match.id,
                        date = match.date,
                        homeTeamId = match.homeTeam,
                        homeTeamName = teamRepository.get(match.homeTeam).name,
                        homeTeamScore = match.homeTeamResult?.scored?.toString() ?: "-",
                        awayTeamId = match.awayTeam,
                        awayTeamName = teamRepository.get(match.awayTeam).name,
                        awayTeamScore = match.awayTeamResult?.scored?.toString() ?: "-",
                    )
                }
                ?: emptyList()
        }.sortedBy { it.date }
    }
}
