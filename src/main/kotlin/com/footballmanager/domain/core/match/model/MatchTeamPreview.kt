package com.footballmanager.domain.core.match.model

import java.util.UUID

data class MatchTeamPreview(
    val manager: UUID?,
    val startPlayers: Set<UUID>,
    val benchPlayers: Set<UUID>,
)