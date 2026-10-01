package com.footballmanager.model.schedule

import com.footballmanager.model.match.entity.Match
import com.footballmanager.model.schedule.entity.Round
import com.footballmanager.model.schedule.repository.RoundRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class RoundService(
    private val roundRepository: RoundRepository,
) {
    fun createRound(
        id: UUID,
        stage: Int,
        matches: List<UUID>,
    ): Round {
        val round = Round(
            id = id,
            stage = stage,
            matches = matches,
        )
        roundRepository.add(round)
        return round
    }
}