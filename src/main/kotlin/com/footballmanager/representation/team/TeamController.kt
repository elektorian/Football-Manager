package com.footballmanager.representation.team

import com.footballmanager.team.TeamService
import com.footballmanager.representation.team.dto.TeamInfo
import com.footballmanager.representation.tournament.dto.MatchInfo
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/teams")
class TeamController(
    private val teamService: TeamService,
) {
    @GetMapping("/{teamId}")
    fun getTeam(@PathVariable("teamId") teamId: UUID): TeamInfo {
        return teamService.getTeamInfo(teamId)
    }

    @GetMapping("/{teamId}/schedule")
    fun getTeamSchedule(@PathVariable("teamId") teamId: UUID): List<MatchInfo> {
        return teamService.getTeamSchedule(teamId)
    }
}