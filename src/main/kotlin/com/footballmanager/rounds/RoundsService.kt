package com.footballmanager.rounds

import com.footballmanager.entities.season.schedule.Round
import org.springframework.stereotype.Service
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Service
class RoundsService {
    private val rounds = ConcurrentHashMap<UUID, Round>()

    fun getRound(roundId: UUID) = rounds[roundId]!!

    fun add(round: Round) {
        rounds[round.id] = round
    }
}
