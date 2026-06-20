package com.footballmanager.team.dto

import com.footballmanager.players.dto.PlayerInfo
import java.util.UUID

data class TeamInfo(
    val id: UUID,
    val name: String,
    val abbreviation: String,
    val city: String,
    val country: String,
    val players: Collection<PlayerInfo>,
)