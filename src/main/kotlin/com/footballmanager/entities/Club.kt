package com.footballmanager.entities

import com.footballmanager.entities.match.Match
import com.footballmanager.tournaments.enumerations.TournamentType
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

data class Club(
    val id: UUID,
    val name: String,
    val abbreviation: String,
    val city: String,
    val country: String,
    // type to tournamentId
    val tournaments: ConcurrentHashMap<TournamentType, UUID> = ConcurrentHashMap<TournamentType, UUID>(),
    val matches: Collection<UUID>,
) {
    fun isParticipant(match: Match): Boolean {
        return match.homeTeam == id || match.awayTeam == id
    }
}
