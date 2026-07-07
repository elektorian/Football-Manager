package com.footballmanager.domain.core.team.model

import com.footballmanager.domain.core.match.model.Match
import com.footballmanager.domain.core.tournament.enumerations.TournamentType
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArraySet

data class Team(
    val id: UUID,
    val name: String,
    val abbreviation: String,
    val city: String,
    val country: String,
    // type to tournamentId
    val tournaments: ConcurrentHashMap<TournamentType, UUID> = ConcurrentHashMap<TournamentType, UUID>(),
    val players: CopyOnWriteArraySet<UUID> = CopyOnWriteArraySet(),
    val staff: Staff = Staff(),
) {
    fun isParticipant(match: Match): Boolean {
        return match.homeTeam == id || match.awayTeam == id
    }
}