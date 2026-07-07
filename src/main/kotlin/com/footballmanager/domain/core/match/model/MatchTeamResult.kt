package com.footballmanager.domain.core.match.model

import com.footballmanager.domain.core.match.enumeration.MatchTeamStatus
import java.util.*

data class MatchTeamResult(
    val team: UUID,
    val scored: Int,
    val conceded: Int,
    val status: MatchTeamStatus,
)
