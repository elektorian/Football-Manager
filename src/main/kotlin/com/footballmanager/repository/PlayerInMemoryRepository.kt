package com.footballmanager.repository

import com.footballmanager.application.repository.PlayerRepository
import com.footballmanager.players.model.Player
import org.springframework.stereotype.Repository
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Repository
class PlayerInMemoryRepository : PlayerRepository {
    private val players = ConcurrentHashMap<UUID, Player>()

    override fun get(id: UUID): Player {
        return players[id]!!
    }

    override fun save(player: Player) {
        players[player.id] = player
    }
}