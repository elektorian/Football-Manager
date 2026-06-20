package com.footballmanager.application.dto

import java.time.LocalDate
import java.util.UUID

data class CoachDto(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val birthDate: LocalDate,
)
