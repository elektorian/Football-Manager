package com.footballmanager.presentation.controller

import com.footballmanager.application.dto.LeagueTeamInfoDto
import com.footballmanager.application.dto.TournamentDto
import com.footballmanager.application.port.input.TournamentUseCase
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/tournaments")
class TournamentController(
    private val tournamentUseCase: TournamentUseCase,
) {
    @GetMapping("/league")
    fun league(
        @RequestParam("leagueId") leagueId: UUID,
        @RequestParam("seasonId") seasonId: UUID?,
    ): Collection<LeagueTeamInfoDto> = tournamentUseCase.getLeagueTable(leagueId, seasonId)

    @GetMapping("/{id}")
    fun getTournament(@PathVariable id: UUID): TournamentDto = tournamentUseCase.getTournament(id)
}
