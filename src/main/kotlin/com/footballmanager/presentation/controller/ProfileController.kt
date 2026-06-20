package com.footballmanager.presentation.controller

import com.footballmanager.application.dto.CoachDto
import com.footballmanager.application.dto.LeagueInfoDto
import com.footballmanager.application.dto.TeamInfoDto
import com.footballmanager.application.port.input.ProfileUseCase
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/profile")
class ProfileController(
    private val profileUseCase: ProfileUseCase,
) {
    @GetMapping("/coach")
    fun coach(): CoachDto = profileUseCase.getCoach()

    @GetMapping("/league")
    fun league(): LeagueInfoDto? = profileUseCase.getLeague()

    @GetMapping("/team")
    fun getTeam(): TeamInfoDto? = profileUseCase.getTeam()
}
