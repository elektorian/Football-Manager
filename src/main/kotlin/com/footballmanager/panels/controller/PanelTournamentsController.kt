package com.footballmanager.panels.controller

import com.footballmanager.tournaments.TournamentService
import com.footballmanager.tournaments.model.LeagueInfo
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/panels/tournaments")
class PanelTournamentsController(
    private val tournamentService: TournamentService,
) {
    @GetMapping("/leaugue/{id}")
    fun league(
        @PathVariable id: UUID,
        @RequestParam year: Int,
    ): LeagueInfo {
        return tournamentService.getLeagueInfo(
            year = year,
            leagueId = id,
        )
    }
}
