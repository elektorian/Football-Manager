package com.footballmanager.entities

import com.footballmanager.entities.match.Match
import com.footballmanager.entities.match.MatchTeamStatus
import java.util.UUID

data class Club(
    val id: UUID,
    val name: String,
    val abbreviation: String,
    val city: String,
    val country: String,
    var leagueSeason: Season?,
    val matches: Collection<Match>,
) {
    fun isParticipant(match: Match): Boolean {
        return match.homeTeam.id == id || match.awayTeam.id == id
    }
}
