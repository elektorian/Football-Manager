package com.footballmanager.entities

import com.footballmanager.entities.match.Match
import java.util.UUID

data class Club(
    val id: UUID,
    val name: String,
    val abbreviation: String,
    val city: String,
    val country: String,
    var leagueSeason: UUID?,
    val matches: Collection<UUID>,
) {
    fun isParticipant(match: Match): Boolean {
        return match.homeTeam == id || match.awayTeam == id
    }
}
