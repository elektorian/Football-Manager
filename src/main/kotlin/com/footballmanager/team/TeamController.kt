package com.footballmanager.team

import com.footballmanager.team.dto.TeamInfo
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/teams")
class TeamController(
    private val teamService: TeamService,
) {
    @GetMapping("/{teamId}")
    fun getTeam(@PathVariable("teamId") teamId: UUID): TeamInfo {
        return teamService.getTeamInfo(teamId)
    }
}