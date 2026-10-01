package com.footballmanager.model.player.entity

import java.util.UUID

data class Contract(
    val id: UUID,
    val player: UUID,
    val club: UUID,
)
