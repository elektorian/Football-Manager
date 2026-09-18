package com.footballmanager.domain.person

import java.util.UUID
import java.time.LocalDate

class Person(
    val id: UUID,
    val name: String,
    val surname: String,
    val birthDate: LocalDate?,
    val clubId: UUID?
)
