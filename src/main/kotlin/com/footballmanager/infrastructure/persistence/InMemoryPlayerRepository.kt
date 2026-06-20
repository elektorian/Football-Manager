package com.footballmanager.infrastructure.persistence

import com.footballmanager.domain.model.Player
import com.footballmanager.domain.repository.PlayerRepository
import org.springframework.stereotype.Repository
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Repository
class InMemoryPlayerRepository : PlayerRepository {
    private val store = ConcurrentHashMap<UUID, Player>()

    override fun findById(id: UUID): Player? = store[id]

    override fun save(player: Player) {
        store[player.id] = player
    }
}
