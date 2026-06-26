package com.footballmanager.domain

data class Standing(
    val clubId: Long,
    val played: Int = 0,
    val wins: Int = 0,
    val draws: Int = 0,
    val losses: Int = 0,
    val goalsFor: Int = 0,
    val goalsAgainst: Int = 0,
    val form: List<String> = emptyList()
) {
    val points: Int get() = wins * 3 + draws
    val goalDifference: Int get() = goalsFor - goalsAgainst
}
