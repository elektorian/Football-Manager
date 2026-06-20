package com.footballmanager.application.port.input

import com.footballmanager.application.dto.PlayerDto
import java.util.UUID

interface PlayerUseCase {
    fun getPlayer(id: UUID): PlayerDto
    fun getPlayersByTeam(teamId: UUID): List<PlayerDto>
}
