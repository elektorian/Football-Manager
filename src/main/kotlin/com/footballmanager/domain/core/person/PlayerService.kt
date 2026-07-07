package com.footballmanager.domain.core.person

import com.footballmanager.domain.core.person.model.Person
import com.footballmanager.domain.dao.PlayerRepository
import com.footballmanager.domain.dao.TeamRepository
import com.footballmanager.representation.domain.player.dto.PlayerInfo
import org.springframework.stereotype.Service
import java.util.*

@Service
class PlayerService(
    private val teamRepository: TeamRepository,
    private val playerRepository: PlayerRepository,
) {
    fun register(player: Person) {
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