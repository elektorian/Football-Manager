package com.footballmanager.model.schedule.entity

import java.util.UUID

data class Round(
    val id: UUID,
    val stage: Int,
    val matches: Collection<UUID>,
    val schedule: UUID,
)
