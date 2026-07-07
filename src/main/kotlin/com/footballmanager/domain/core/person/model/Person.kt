package com.footballmanager.domain.core.person.model

import java.time.LocalDate
import java.util.UUID

data class Person(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val nickname: String? = null,
    val birthDate: LocalDate,
    val contract: PersonContract?,
)
