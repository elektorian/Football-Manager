package com.footballmanager.domain.dao.inMemory

import com.footballmanager.domain.core.person.model.Person
import com.footballmanager.domain.dao.PlayerRepository
import org.springframework.stereotype.Repository
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Repository
class PlayerInMemoryRepository : PlayerRepository {
    private val players = ConcurrentHashMap<UUID, Person>()

    override fun get(id: UUID): Person {
        return players[id]!!
    }

    override fun save(person: Person) {
        players[person.id] = person
    }
}