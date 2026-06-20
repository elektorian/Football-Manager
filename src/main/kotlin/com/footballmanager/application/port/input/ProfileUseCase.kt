package com.footballmanager.application.port.input

import com.footballmanager.application.dto.CoachDto
import com.footballmanager.application.dto.LeagueInfoDto
import com.footballmanager.application.dto.TeamInfoDto

interface ProfileUseCase {
    fun getCoach(): CoachDto
    fun getLeague(): LeagueInfoDto?
    fun getTeam(): TeamInfoDto?
}
