package com.footballmanager.entities.season.schedule

import com.footballmanager.entities.match.Match
import com.footballmanager.entities.season.Season

data class Round(
    val matches: List<Match>,
    val number: Int,
    var passed: Boolean,
    val season: Season,
)
