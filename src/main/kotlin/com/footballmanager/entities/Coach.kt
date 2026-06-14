package com.footballmanager.entities

import java.time.LocalDate
import java.util.UUID

data class Coach(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val birthDate: LocalDate,
)
