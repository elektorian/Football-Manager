package com.footballmanager.api

data class EngineStateResponse(
    val date: String,
    val club: ClubInfo?,
    val nextMatch: MatchInfo?,
    val leagueTable: List<StandingRow>,
    val recentForm: List<String>,
    val upcomingFixtures: List<MatchInfo>,
    val events: List<String>? = null
)

data class ClubInfo(
    val id: Long,
    val name: String,
    val shortName: String
)

data class MatchInfo(
    val fixtureId: Long,
    val opponent: String,
    val venue: String,
    val date: String,
    val competition: String
)

data class StandingRow(
    val position: Int,
    val clubId: Long,
    val clubName: String,
    val played: Int,
    val wins: Int,
    val draws: Int,
    val losses: Int,
    val goalsFor: Int,
    val goalsAgainst: Int,
    val points: Int,
    val gd: Int,
    val form: List<String>
)
