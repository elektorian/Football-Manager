package com.footballmanager.domain.repository

import com.footballmanager.domain.person.Person
import java.util.UUID

interface PersonRepository {
    fun save(person: Person): Person
    fun get(id: UUID): Person
}
