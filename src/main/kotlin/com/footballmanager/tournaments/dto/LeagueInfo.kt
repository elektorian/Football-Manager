package com.footballmanager.tournaments.dto

data class LeagueInfo(
    val leagueName: String,
    val table: Collection<LeagueTeamInfo>,
)