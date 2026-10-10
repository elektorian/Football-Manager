package com.footballmanager.model.season.entity

import java.util.UUID

data class Season(
    val id: UUID,
    val schedule: UUID,
    val clubs: List<UUID>,
    val year: Int,
    val tournament: UUID,
)
