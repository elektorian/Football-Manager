package com.footballmanager.domain.core.notification.payload.roundPreview.dto

import com.footballmanager.domain.core.notification.payload.NotificationPayload

data class RoundPreviewPayload(
    val tournamentName: String,
    val matches: List<MatchPair>,
) : NotificationPayload
