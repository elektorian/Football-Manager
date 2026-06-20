package com.footballmanager.players.dto

import java.time.LocalDate
import java.util.UUID

data class PlayerInfo(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val nickname: String? = null,
    val birthDate: LocalDate,
)
