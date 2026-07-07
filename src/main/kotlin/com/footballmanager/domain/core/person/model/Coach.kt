package com.footballmanager.domain.core.person.model

import java.time.LocalDate
import java.util.UUID

data class Coach( // todo remove, use person instead
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val birthDate: LocalDate,
)