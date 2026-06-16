package com.footballmanager.profile

import com.footballmanager.entities.Club
import com.footballmanager.entities.Coach
import com.footballmanager.entities.League
import com.footballmanager.matches.MatchesService
import com.footballmanager.rounds.RoundsService
import com.footballmanager.seasons.ScheduleService
import com.footballmanager.seasons.SeasonService
import com.footballmanager.session.SessionState
import com.footballmanager.tournaments.TournamentsService
import com.footballmanager.tournaments.dto.LeagueInfo
import com.footballmanager.tournaments.dto.MatchInfo
import com.footballmanager.tournaments.dto.RoundInfo
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@RestController
@RequestMapping("/profile")
class ProfileController(
    private val sessionState: SessionState,
    private val tournamentsService: TournamentsService,
    private val seasonService: SeasonService,
    private val scheduleService: ScheduleService,
    private val leagues: ConcurrentHashMap<UUID, League>,
    private val matchesService: MatchesService,
    private val teams: ConcurrentHashMap<UUID, Club>,
    private val roundsService: RoundsService,
) {
    @GetMapping("/coach")
    fun coach(): Coach = sessionState.player

    @GetMapping("/league")
    fun league(): LeagueInfo? {
        if (sessionState.club == null) return null
        if (sessionState.club!!.leagueSeason == null) return null
        val season = sessionState.club!!.leagueSeason!!.let { seasonService.getSeason(it) }
        val league = leagues[season.league]!!
        return LeagueInfo(
            leagueName = league.name,
            table = tournamentsService.getLeagueTable(
                leagueId = league.id,
                seasonId = season.id,
            ),
            rounds = season.schedule?.let { scheduleService.getSchedule(it) }?.rounds?.map { roundId ->
                val round = roundsService.getRound(roundId)
                RoundInfo(
                    number = round.number,
                    passed = round.passed,
                    matches = round.matches.map { matchesService.getMatch(it) }.map { match ->
                        MatchInfo(
                            id = match.id,
                            date = match.date,
                            homeTeamId = match.homeTeam,
                            homeTeamName = teams[match.homeTeam]!!.name,
                            homeTeamScore = match.homeTeamResult?.scored?.toString() ?: "-",
                            awayTeamId = match.awayTeam,
                            awayTeamName = teams[match.awayTeam]!!.name,
                            awayTeamScore = match.awayTeamResult?.scored?.toString() ?: "-",
                        )
                    }
                )
            },
        )
    }
}