package com.footballmanager.application.dto

import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

data class PlayerDto(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val nickname: String? = null,
    val birthDate: LocalDate,
    val salary: BigDecimal? = null,
)
