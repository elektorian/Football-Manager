package com.footballmanager.representation.domain.tournament.dto

data class RoundInfo(
    val matches: List<MatchInfo>,
    val number: Int,
    var passed: Boolean,
)
