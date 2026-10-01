package com.footballmanager.model.schedule.repository

import com.footballmanager.model.schedule.entity.Round
import org.springframework.stereotype.Repository
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Repository
class RoundRepository {
    private val rounds = ConcurrentHashMap<UUID, Round>()

    fun add(round: Round){
        rounds[round.id] = round
    }
}