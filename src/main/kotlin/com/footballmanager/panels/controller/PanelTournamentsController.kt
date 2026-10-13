package com.footballmanager.panels.controller

import com.footballmanager.tournaments.LeagueTableCalculator
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
    private val leagueTableCalculator: LeagueTableCalculator,
) {
    @GetMapping("/league/{id}")
    fun league(
        @PathVariable id: UUID,
        @RequestParam year: Int? = null,
    ): LeagueInfo {
        return leagueTableCalculator.getLeagueInfo(
            year = year,
            leagueId = id,
        )
    }
}
