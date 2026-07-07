package com.footballmanager.representation.domain.tournament

import com.footballmanager.domain.dao.TournamentRepository
import com.footballmanager.domain.functions.LeagueTableFunction
import com.footballmanager.representation.domain.tournament.dto.TournamentInfo
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/tournaments")
class TournamentsController(
    private val leagueTableFunction: LeagueTableFunction,
    private val tournamentRepository: TournamentRepository,
) {
    @GetMapping("/league")
    fun league(
        @RequestParam("leagueId") leagueId: UUID,
        @RequestParam("seasonId") seasonId: UUID?,
    ) = leagueTableFunction.getLeagueTable(leagueId, seasonId)

    @GetMapping("/{id}")
    fun getTournament(@PathVariable id: UUID): TournamentInfo {
        val league = tournamentRepository.get(id)
        return TournamentInfo(id = league.id, name = league.name)
    }
}