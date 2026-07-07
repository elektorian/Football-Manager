package com.footballmanager.domain.dao.inMemory

import com.footballmanager.domain.core.players.model.Player
import com.footballmanager.domain.dao.PlayerRepository
import org.springframework.stereotype.Repository
import java.util.*
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