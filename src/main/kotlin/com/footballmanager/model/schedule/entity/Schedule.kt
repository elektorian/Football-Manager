package com.footballmanager.model.schedule.entity

import java.util.UUID

data class Schedule(
    val id: UUID,
    val rounds: Collection<UUID>,
)
