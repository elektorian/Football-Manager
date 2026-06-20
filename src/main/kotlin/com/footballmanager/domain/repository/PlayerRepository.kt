package com.footballmanager.domain.repository

import com.footballmanager.domain.model.Player
import java.util.UUID

interface PlayerRepository {
    fun findById(id: UUID): Player?
    fun save(player: Player)
}
