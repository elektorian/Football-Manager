package com.footballmanager.model.club

import com.footballmanager.model.club.entity.Club
import com.footballmanager.model.club.repository.ClubRepository
import org.springframework.stereotype.Service
import java.util.UUID
import java.util.concurrent.CopyOnWriteArraySet

@Service
class ClubService(
    private val clubRepository: ClubRepository,
) {
    fun createClub(
        name: String,
        id: UUID,
        players: CopyOnWriteArraySet<UUID>,
    ): Club {
        val club = Club(
            id = id,
            name = name,
            players = players,
        )
        clubRepository.addClub(club)
        return club
    }
}