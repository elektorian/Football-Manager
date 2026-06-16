package com.footballmanager.team

import com.footballmanager.entities.Club
import java.util.UUID

data class ClubsSourceData(
    val leagueId: UUID,
    val clubs: List<Club>,
)