package com.footballmanager.domain.core.notification.payload.roundPreview.dto

import java.util.UUID

data class MatchPair(
    val homeTeamId: UUID,
    val homeTeam: String,
    val awayTeamId: UUID,
    val awayTeam: String,
    val homePosition: Int,
    val awayPosition: Int,
)
