package com.footballmanager.representation.domain.player

import com.footballmanager.domain.core.person.PlayerService
import com.footballmanager.domain.dao.TeamRepository
import com.footballmanager.representation.domain.player.dto.PlayerInfo
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/players")
class PlayerController(
    private val playerService: PlayerService,
    private val teamRepository: TeamRepository,
) {
    @GetMapping("{id}")
    fun getPlayer(@PathVariable id: UUID): PlayerInfo {
        return playerService.getPlayerInfo(id)
    }

    @GetMapping
    fun getPlayers(@RequestParam team: UUID): List<PlayerInfo> {
        val players = teamRepository.get(team).players
        return players.map { playerService.getPlayerInfo(it) }
    }
}