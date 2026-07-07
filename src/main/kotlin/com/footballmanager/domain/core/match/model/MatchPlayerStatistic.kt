package com.footballmanager.domain.core.match.model

import java.util.UUID

data class MatchPlayerStatistic(
    val player: UUID,
    val goals: Int,
    val assists: Int,
    val score: Double,
)
