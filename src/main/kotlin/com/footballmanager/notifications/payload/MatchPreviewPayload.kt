package com.footballmanager.notifications.payload

data class MatchPreviewPayload(
    val tournamentName: String,
    val matches: List<MatchPair>,
) : NotificationPayload

data class MatchPair(
    val homeTeam: String,
    val awayTeam: String,
    val homePosition: Int,
    val awayPosition: Int,
)
