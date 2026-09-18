package com.footballmanager.infrastructure.storage

import com.footballmanager.domain.club.Club
import com.footballmanager.domain.repository.ClubRepository
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class InMemoryClubRepository : ClubRepository {
    private val store = ConcurrentHashMap<UUID, Club>()

    override fun save(club: Club): Club {
        store[club.id] = club
        return club
    }

    override fun get(id: UUID): Club {
        return store[id]!!
    }
}
