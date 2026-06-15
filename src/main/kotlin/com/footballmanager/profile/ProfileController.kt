package com.footballmanager.profile

import com.footballmanager.entities.Coach
import com.footballmanager.session.SessionState
import com.footballmanager.tournaments.TournamentsService
import com.footballmanager.tournaments.dto.LeagueInfo
import com.footballmanager.tournaments.dto.LeagueTeamInfo
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/profile")
class ProfileController(
    private val sessionState: SessionState,
    private val tournamentsService: TournamentsService,
) {
    @GetMapping("/coach")
    fun coach(): Coach = sessionState.player

    @GetMapping("/league")
    fun league(): LeagueInfo? {
        if (sessionState.club == null) return null
        if (sessionState.club!!.leagueSeason == null) return null
        val season = sessionState.club!!.leagueSeason!!
        return LeagueInfo(
            leagueName = season.league.name,
            table = tournamentsService.getLeagueTable(
                leagueId = season.league.id,
                seasonId = season.id,
            ),
        )
    }
}