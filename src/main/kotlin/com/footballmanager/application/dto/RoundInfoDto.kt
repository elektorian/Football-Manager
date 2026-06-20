package com.footballmanager.application.dto

data class RoundInfoDto(
    val matches: List<MatchInfoDto>,
    val number: Int,
    var passed: Boolean,
)
