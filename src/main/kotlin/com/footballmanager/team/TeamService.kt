package com.footballmanager.team

import com.footballmanager.entities.Club
import com.footballmanager.players.PlayerService
import com.footballmanager.team.dto.TeamInfo
import org.springframework.stereotype.Service
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Service
class TeamService(
    private val teams: ConcurrentHashMap<UUID, Club>,
    private val playerService: PlayerService,
) {
    fun getTeamInfo(id: UUID): TeamInfo {
        val team = teams[id]!!
        return TeamInfo(
            id = team.id,
            name = team.name,
            abbreviation = team.abbreviation,
            city = team.city,
            country = team.country,
            players = team.players.map { playerService.getPlayerInfo(it) }
        )
    }
}
