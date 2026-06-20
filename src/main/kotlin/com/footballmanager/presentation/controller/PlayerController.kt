package com.footballmanager.presentation.controller

import com.footballmanager.application.dto.PlayerDto
import com.footballmanager.application.port.input.PlayerUseCase
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/players")
class PlayerController(
    private val playerUseCase: PlayerUseCase,
) {
    @GetMapping("{id}")
    fun getPlayer(@PathVariable id: UUID): PlayerDto = playerUseCase.getPlayer(id)

    @GetMapping
    fun getPlayers(@RequestParam team: UUID): List<PlayerDto> = playerUseCase.getPlayersByTeam(team)
}
