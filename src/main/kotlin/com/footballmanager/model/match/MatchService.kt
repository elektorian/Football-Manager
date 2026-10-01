package com.footballmanager.model.match

import com.footballmanager.model.match.entity.Match
import com.footballmanager.model.match.repository.MatchRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@Service
class MatchService(
    private val matchRepository: MatchRepository,
) {
    fun create(
        id: UUID,
        date: LocalDate,
        homeTeam: UUID,
        awayTeam: UUID,
    ): Match {
        val match = Match(
            id = id,
            date = date,
            homeTeam = homeTeam,
            awayTeam = awayTeam,
        )
        matchRepository.addMatch(match)
        return match
    }
}