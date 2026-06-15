package com.footballmanager.entities

import com.footballmanager.entities.match.Match
import java.util.UUID
import java.util.concurrent.CopyOnWriteArraySet

data class Season(
    val id : UUID,
    val league: League,
    val year: Int,
    val clubs: CopyOnWriteArraySet<Club>,
    val matches: CopyOnWriteArraySet<Match>,
)
