package com.footballmanager.domain.core.match.model

import com.footballmanager.domain.core.match.enumeration.MatchTeamStatus

data class MatchTeamResult(
    val scored: Int,
    val conceded: Int,
    val status: MatchTeamStatus,
    val playersStatistic: Collection<MatchPlayerStatistic>,
)
