package com.footballmanager.domain.core.schedule.model

import com.footballmanager.domain.core.season.model.Season
import java.util.*

data class Round(
    val matches: List<UUID>,
    val number: Int,
    @Volatile
    var passed: Boolean,
    val season: Season,
    val id: UUID,
)