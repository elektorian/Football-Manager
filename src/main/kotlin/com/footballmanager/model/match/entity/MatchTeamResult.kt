package com.footballmanager.model.match.entity

import java.util.UUID

data class MatchTeamResult(
    val clubId: UUID,
    val scored: Int,
)
