package com.footballmanager.domain.core.team

import com.footballmanager.domain.dao.MatchRepository
import com.footballmanager.domain.dao.TeamRepository
import com.footballmanager.domain.functions.TournamentScheduleFunction
import com.footballmanager.representation.domain.team.dto.TeamInfo
import com.footballmanager.representation.domain.tournament.dto.MatchInfo
import org.springframework.stereotype.Service
import java.util.*

@Service
class TeamService(
    private val teamRepository: TeamRepository,
    private val matchRepository: MatchRepository,
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
        val team = teamRepository.get(teamId)

        return team.tournaments.values.flatMap { tournamentId ->
            tournamentScheduleFunction.execute(tournamentId)
                ?.flatMap { round -> round.matches.map { matchRepository.get(it) } }
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
