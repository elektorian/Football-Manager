package com.footballmanager.players

import com.footballmanager.application.repository.TeamRepository
import com.footballmanager.entities.Team
import com.footballmanager.players.dto.PlayerInfo
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

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