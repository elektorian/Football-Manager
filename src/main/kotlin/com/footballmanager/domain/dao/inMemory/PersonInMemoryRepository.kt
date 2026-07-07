package com.footballmanager.domain.dao.inMemory

import com.footballmanager.domain.core.person.model.Person
import com.footballmanager.domain.dao.PersonRepository
import org.springframework.stereotype.Repository
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Repository
class PersonInMemoryRepository : PersonRepository {
    private val persons = ConcurrentHashMap<UUID, Person>()

    override fun get(id: UUID): Person {
        return persons[id]!!
    }

    override fun merge(person: Person) {
        persons[person.id] = person
    }
}