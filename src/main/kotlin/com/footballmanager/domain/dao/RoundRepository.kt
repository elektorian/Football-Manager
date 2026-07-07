package com.footballmanager.domain.dao

import com.footballmanager.domain.core.schedule.model.Round
import java.util.*

interface
RoundRepository {
    fun get(id: UUID): Round
    fun add(round: Round)
}