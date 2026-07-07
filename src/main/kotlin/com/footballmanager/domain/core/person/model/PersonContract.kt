package com.footballmanager.domain.core.person.model

import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

data class PersonContract(
    val id: UUID,
    val team: UUID,
    val startDate: LocalDate,
    val expiryDate: LocalDate,
    val salary: BigDecimal, // per week
)
