package com.footballmanager.tournaments

import com.footballmanager.entities.League
import com.footballmanager.functions.LeagueTableFunction
import com.footballmanager.tournaments.dto.TournamentInfo
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@RestController
@RequestMapping("/tournaments")
class TournamentsController(
    private val leagueTableFunction: LeagueTableFunction,
    private val leagues: ConcurrentHashMap<UUID, League>,
) {
    @GetMapping("/league")
    fun league(
        @RequestParam("leagueId") leagueId: UUID,
        @RequestParam("seasonId") seasonId: UUID?,
    ) = leagueTableFunction.getLeagueTable(leagueId, seasonId)

    @GetMapping("/{id}")
    fun getTournament(@PathVariable id: UUID): TournamentInfo {
        val league = leagues[id] ?: throw IllegalStateException("Tournament not found")
        return TournamentInfo(id = league.id, name = league.name)
    }
}