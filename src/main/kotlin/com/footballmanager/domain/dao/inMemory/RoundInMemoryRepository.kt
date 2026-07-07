package com.footballmanager.domain.dao.inMemory

import com.footballmanager.domain.core.schedule.model.Round
import com.footballmanager.domain.dao.RoundRepository
import org.springframework.stereotype.Repository
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Repository
class RoundInMemoryRepository : RoundRepository {
    private val rounds = ConcurrentHashMap<UUID, Round>()

    override fun get(id: UUID): Round {
        return rounds[id]!!
    }

    override fun add(round: Round) {
        rounds[round.id] = round
    }
}