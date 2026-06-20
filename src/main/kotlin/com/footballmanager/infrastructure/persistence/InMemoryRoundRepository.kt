package com.footballmanager.infrastructure.persistence

import com.footballmanager.domain.model.Round
import com.footballmanager.domain.repository.RoundRepository
import org.springframework.stereotype.Repository
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Repository
class InMemoryRoundRepository : RoundRepository {
    private val store = ConcurrentHashMap<UUID, Round>()

    override fun findById(id: UUID): Round? = store[id]

    override fun save(round: Round) {
        store[round.id] = round
    }
}
