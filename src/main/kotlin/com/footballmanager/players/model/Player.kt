package com.footballmanager.players.model

import java.time.LocalDate
import java.util.UUID

data class Player(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val nickname: String? = null,
    val birthDate: LocalDate,
    val contract: PlayerContract?,
)
