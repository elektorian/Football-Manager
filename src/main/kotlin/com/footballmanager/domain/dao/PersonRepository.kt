package com.footballmanager.domain.dao

import com.footballmanager.domain.core.person.model.Person
import java.util.UUID

interface PersonRepository {
    fun get(id: UUID): Person
    fun merge(person: Person)
}