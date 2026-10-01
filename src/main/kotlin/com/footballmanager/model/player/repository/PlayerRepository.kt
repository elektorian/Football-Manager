package com.footballmanager.model.player.repository

import com.footballmanager.model.club.entity.Club
import com.footballmanager.model.match.entity.Match
import com.footballmanager.model.player.entity.Player
import org.springframework.stereotype.Repository
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Repository
class PlayerRepository {
    private val players = ConcurrentHashMap<UUID, Player>()

    fun addPlayer(player: Player) {
        players[player.id] = player
    }
}