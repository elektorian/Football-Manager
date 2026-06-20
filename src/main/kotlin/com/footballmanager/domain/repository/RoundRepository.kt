package com.footballmanager.domain.repository

import com.footballmanager.domain.model.Round
import java.util.UUID

interface RoundRepository {
    fun findById(id: UUID): Round?
    fun save(round: Round)
}
