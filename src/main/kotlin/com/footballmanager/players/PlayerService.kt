package com.footballmanager.players

import com.footballmanager.application.repository.PlayerRepository
import com.footballmanager.application.repository.TeamRepository
import com.footballmanager.entities.Team
import com.footballmanager.players.dto.PlayerInfo
import com.footballmanager.players.model.Player
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Service
class PlayerService(
    private val teamRepository: TeamRepository,
    private val playerRepository: PlayerRepository,
) {
    fun register(player: Player) {
        playerRepository.save(player)
        if (player.contract == null) return
        val team = teamRepository.get(player.contract.team)
        team.players.add(player.id)
    }

    fun getPlayerInfo(id: UUID): PlayerInfo {
        val player = playerRepository.get(id)
        return PlayerInfo(
            id = player.id,
            firstName = player.firstName,
            lastName = player.lastName,
            nickname = player.nickname,
            birthDate = player.birthDate,
            salary = player.contract?.salary,
        )
    }
}