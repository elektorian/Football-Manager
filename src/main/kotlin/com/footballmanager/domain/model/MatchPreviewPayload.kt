package com.footballmanager.domain.model

import java.util.UUID

data class MatchPreviewPayload(
    val tournamentName: String,
    val matches: List<MatchPair>,
) : NotificationPayload

data class MatchPair(
    val homeTeamId: UUID,
    val homeTeam: String,
    val awayTeamId: UUID,
    val awayTeam: String,
    val homePosition: Int,
    val awayPosition: Int,
)
