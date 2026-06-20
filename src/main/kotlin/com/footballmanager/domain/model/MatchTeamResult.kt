package com.footballmanager.domain.model

import java.util.UUID

data class MatchTeamResult(
    val team: UUID,
    val scored: Int,
    val conceded: Int,
    val status: MatchTeamStatus,
)
