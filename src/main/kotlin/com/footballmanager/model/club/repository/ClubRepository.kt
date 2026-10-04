package com.footballmanager.model.club.repository

import com.footballmanager.model.club.entity.Club
import org.springframework.stereotype.Repository
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Repository
class ClubRepository {
    private val clubs = ConcurrentHashMap<UUID, Club>()

    fun addClub(club: Club) {
        clubs[club.id] = club
    }

    fun getClub(id: UUID): Club = clubs[id]!!
}