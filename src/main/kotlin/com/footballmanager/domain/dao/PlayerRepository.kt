package com.footballmanager.domain.dao

import com.footballmanager.domain.core.person.model.Person
import java.util.UUID

interface PlayerRepository {
    fun get(id: UUID): Person
    fun save(person: Person)
}