package com.footballmanager.application.port.input

import com.footballmanager.application.dto.MatchInfoDto
import com.footballmanager.application.dto.TeamInfoDto
import java.util.UUID

interface TeamUseCase {
    fun getTeam(id: UUID): TeamInfoDto
    fun getTeamSchedule(teamId: UUID): List<MatchInfoDto>
}
