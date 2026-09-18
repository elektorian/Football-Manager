package com.footballmanager.domain.repository

import com.footballmanager.domain.club.Club
import java.util.UUID

interface ClubRepository {
    fun save(club: Club): Club
    fun get(id: UUID): Club
}
