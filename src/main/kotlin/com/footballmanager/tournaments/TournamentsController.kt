package com.footballmanager.tournaments

import com.footballmanager.functions.LeagueTableFunction
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/tournaments")
class TournamentsController(
    private val leagueTableFunction: LeagueTableFunction,
) {
    @GetMapping("/league")
    fun league(
        @RequestParam("leagueId") leagueId: UUID,
        @RequestParam("seasonId") seasonId: UUID?,
    ) = leagueTableFunction.getLeagueTable(leagueId, seasonId)
}