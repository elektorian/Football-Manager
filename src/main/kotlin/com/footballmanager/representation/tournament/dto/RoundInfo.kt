package com.footballmanager.representation.tournament.dto

data class RoundInfo(
    val matches: List<MatchInfo>,
    val number: Int,
    var passed: Boolean,
)
