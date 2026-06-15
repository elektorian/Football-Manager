package com.footballmanager.tournaments.dto

data class LeagueTeamInfo(
    val name: String,
    val victories: Int,
    val draws: Int,
    val losses: Int,
    val goalsScored: Int,
    val goalsConceded: Int,
    val position: Int,
    val points: Int,
)
