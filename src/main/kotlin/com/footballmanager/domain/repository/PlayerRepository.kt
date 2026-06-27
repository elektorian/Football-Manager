package com.footballmanager.domain.repository

import com.footballmanager.players.model.Player
import java.util.UUID

interface PlayerRepository {
    fun get(id: UUID): Player
    fun save(player: Player)
}