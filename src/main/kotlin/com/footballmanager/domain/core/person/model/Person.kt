package com.footballmanager.domain.core.person.model

import com.footballmanager.domain.core.person.enumeration.PlayerPosition
import java.time.LocalDate
import java.util.UUID
import java.util.concurrent.CopyOnWriteArraySet

data class Person(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val nickname: String? = null,
    val birthDate: LocalDate,
    val contract: PersonContract?,
    val positions: CopyOnWriteArraySet<PlayerPosition>,
    val staffDetails: PersonStaffDetails? = null,
)
