package com.footballmanager.application.repository

import com.footballmanager.domain.repository.RoundRepository
import com.footballmanager.entities.season.schedule.Round
import org.springframework.stereotype.Repository
import java.util.UUID
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