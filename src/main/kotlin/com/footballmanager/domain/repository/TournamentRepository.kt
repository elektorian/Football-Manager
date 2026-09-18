package com.footballmanager.domain.repository

import com.footballmanager.domain.tournament.Tournament
import java.util.UUID

interface TournamentRepository {
    fun save(tournament: Tournament): Tournament
    fun get(id: UUID): Tournament
}
