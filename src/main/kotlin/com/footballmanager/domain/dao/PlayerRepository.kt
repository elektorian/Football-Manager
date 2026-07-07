package com.footballmanager.domain.dao

import com.footballmanager.domain.core.players.model.Player
import java.util.UUID

interface PlayerRepository {
    fun get(id: UUID): Player
    fun save(player: Player)
}