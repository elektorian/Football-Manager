package com.footballmanager.domain.model

import java.util.UUID

data class Team(
    val id: UUID,
    val name: String,
    val abbreviation: String,
    val city: String,
    val country: String,
    val tournaments: MutableMap<String, UUID> = HashMap(),
    val players: MutableSet<UUID> = HashSet(),
) {
    fun isParticipant(match: Match): Boolean {
        return match.homeTeam == id || match.awayTeam == id
    }
}
