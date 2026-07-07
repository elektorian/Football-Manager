package com.footballmanager.domain.core.match

import com.footballmanager.domain.core.match.model.MatchTeamPreview
import com.footballmanager.domain.core.tactic.enumeration.TacticSchema
import com.footballmanager.domain.dao.PersonRepository
import com.footballmanager.domain.dao.TeamRepository
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class MatchTeamSquadProvider(
    private val teamRepository: TeamRepository,
    private val personRepository: PersonRepository,
) {
    fun prepareSquad(team: UUID): MatchTeamPreview {
        val team = teamRepository.get(team)
        val players = team.players
        if (players.count() < 11) throw IllegalStateException("Not enough players to team")
        val manager = team.staff.manager?.let { personRepository.get(it) }
        val schema = manager?.staffDetails?.tacticSchema ?: TacticSchema.entries.random()
        val remainingPlayers = team.players.mapTo(mutableListOf()) { personRepository.get(it) }
        val startPlayers = mutableSetOf<UUID>()
        schema.positions.forEach { (position, _) ->
            val candidates = remainingPlayers.filter { it.positions.contains(position) }
            val finalCandidate = candidates.randomOrNull() ?: remainingPlayers.random()
            startPlayers.add(finalCandidate.id)
            remainingPlayers.remove(finalCandidate)
        }
        val benchPlayers = remainingPlayers.shuffled().take(7).map { it.id }.toSet()
        return MatchTeamPreview(
            manager = manager?.id,
            startPlayers = startPlayers,
            benchPlayers = benchPlayers,
        )
    }
}