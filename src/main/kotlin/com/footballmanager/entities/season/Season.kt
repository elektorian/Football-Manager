package com.footballmanager.entities.season

import java.util.*
import java.util.concurrent.CopyOnWriteArraySet

data class Season(
    val id : UUID,
    val league: UUID,
    val year: Int,
    val clubs: CopyOnWriteArraySet<UUID>,
    val matches: CopyOnWriteArraySet<UUID>,
    var schedule: UUID? = null,
)
