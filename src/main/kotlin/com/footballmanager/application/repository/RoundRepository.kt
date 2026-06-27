package com.footballmanager.application.repository

import com.footballmanager.entities.season.schedule.Round
import java.util.UUID

interface RoundRepository {
    fun get(id: UUID): Round
    fun add(round: Round)
}