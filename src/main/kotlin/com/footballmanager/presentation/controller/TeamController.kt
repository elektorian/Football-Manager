package com.footballmanager.presentation.controller

import com.footballmanager.application.dto.MatchInfoDto
import com.footballmanager.application.dto.TeamInfoDto
import com.footballmanager.application.port.input.TeamUseCase
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/teams")
class TeamController(
    private val teamUseCase: TeamUseCase,
) {
    @GetMapping("/{teamId}")
    fun getTeam(@PathVariable("teamId") teamId: UUID): TeamInfoDto = teamUseCase.getTeam(teamId)

    @GetMapping("/{teamId}/schedule")
    fun getTeamSchedule(@PathVariable("teamId") teamId: UUID): List<MatchInfoDto> = teamUseCase.getTeamSchedule(teamId)
}
