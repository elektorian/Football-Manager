package com.footballmanager.infrastructure.storage

import com.footballmanager.domain.person.Person
import com.footballmanager.domain.repository.PersonRepository
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class InMemoryPersonRepository : PersonRepository {
    private val store = ConcurrentHashMap<UUID, Person>()

    override fun save(person: Person): Person {
        store[person.id] = person
        return person
    }

    override fun get(id: UUID): Person {
        return store[id]!!
    }
}
