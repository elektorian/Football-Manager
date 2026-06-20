package com.footballmanager.entities.season.schedule

import com.footballmanager.entities.season.Season
import java.util.UUID

data class Round(
    val matches: List<UUID>,
    val number: Int,
    @Volatile
    var passed: Boolean,
    val season: Season,
    val id: UUID,
)
