package com.footballmanager.domain.service

import com.footballmanager.domain.model.Match
import com.footballmanager.domain.model.MatchTeamStatus
import com.footballmanager.domain.model.Team
import org.springframework.stereotype.Component
import java.util.UUID

data class LeagueTableEntry(
    val teamId: UUID,
    val name: String,
    val victories: Int,
    val draws: Int,
    val losses: Int,
    val goalsScored: Int,
    val goalsConceded: Int,
    val position: Int,
    val points: Int,
)

@Component
class LeagueTableCalculator {
    fun calculate(
        teamIds: Set<UUID>,
        teamNames: Map<UUID, String>,
        matches: List<Match>,
    ): List<LeagueTableEntry> {
        return teamIds
            .map { teamId ->
                val team = Team(id = teamId, name = teamNames[teamId] ?: "", abbreviation = "", city = "", country = "")
                val participantMatches = matches
                    .filter { team.isParticipant(it) }
                    .filter { it.passed() }
                    .map { it.getResult(team) }
                val victories = participantMatches.count { it.status == MatchTeamStatus.WINNER }
                val draws = participantMatches.count { it.status == MatchTeamStatus.DRAW }
                val losses = participantMatches.count { it.status == MatchTeamStatus.LOSER }
                LeagueTableEntry(
                    teamId = teamId,
                    name = teamNames[teamId] ?: "",
                    victories = victories,
                    draws = draws,
                    losses = losses,
                    goalsScored = participantMatches.sumOf { it.scored },
                    goalsConceded = participantMatches.sumOf { it.conceded },
                    position = 0,
                    points = victories * 3 + draws,
                )
            }
            .sortedByDescending { it.points }
            .mapIndexed { index, entry -> entry.copy(position = index + 1) }
    }
}
