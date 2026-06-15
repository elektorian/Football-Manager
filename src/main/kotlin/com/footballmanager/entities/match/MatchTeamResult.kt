package com.footballmanager.entities.match

import com.footballmanager.entities.Club

data class MatchTeamResult(
    val team: Club,
    val scored: Int,
    val conceded: Int,
    val status: MatchTeamStatus,
)
