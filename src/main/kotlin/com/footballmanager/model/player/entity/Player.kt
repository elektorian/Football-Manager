package com.footballmanager.model.player.entity

import java.util.UUID

data class Player(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val contract: UUID?,
)
