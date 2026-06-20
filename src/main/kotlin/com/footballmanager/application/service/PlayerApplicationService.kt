package com.footballmanager.application.service

import com.footballmanager.application.dto.PlayerDto
import com.footballmanager.application.port.input.PlayerUseCase
import com.footballmanager.domain.model.Player
import com.footballmanager.domain.repository.PlayerRepository
import com.footballmanager.domain.repository.TeamRepository
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class PlayerApplicationService(
    private val playerRepository: PlayerRepository,
    private val teamRepository: TeamRepository,
) : PlayerUseCase {

    override fun getPlayer(id: UUID): PlayerDto {
        val player = playerRepository.findById(id) ?: throw IllegalStateException("Player not found")
        return toDto(player)
    }

    override fun getPlayersByTeam(teamId: UUID): List<PlayerDto> {
        val team = teamRepository.findById(teamId) ?: throw IllegalStateException("Team not found")
        return team.players.mapNotNull { playerRepository.findById(it) }.map { toDto(it) }
    }

    private fun toDto(player: Player) = PlayerDto(
        id = player.id,
        firstName = player.firstName,
        lastName = player.lastName,
        nickname = player.nickname,
        birthDate = player.birthDate,
        salary = player.contract?.salary,
    )
}
