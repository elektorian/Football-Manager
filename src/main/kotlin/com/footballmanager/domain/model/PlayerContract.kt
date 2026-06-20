package com.footballmanager.domain.model

import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

data class PlayerContract(
    val id: UUID,
    val team: UUID,
    val startDate: LocalDate,
    val expiryDate: LocalDate,
    val salary: BigDecimal,
)
