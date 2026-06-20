package com.footballmanager.profile

import com.footballmanager.entities.Club
import com.footballmanager.entities.Coach
import com.footballmanager.entities.League
import com.footballmanager.functions.LeagueTableFunction
import com.footballmanager.functions.TournamentCurrentSeasonFunction
import com.footballmanager.matches.MatchesService
import com.footballmanager.rounds.RoundsService
import com.footballmanager.seasons.ScheduleService
import com.footballmanager.session.SessionContext
import com.footballmanager.team.TeamService
import com.footballmanager.team.dto.TeamInfo
import com.footballmanager.tournaments.dto.LeagueInfo
import com.footballmanager.tournaments.dto.MatchInfo
import com.footballmanager.tournaments.dto.RoundInfo
import com.footballmanager.tournaments.enumerations.TournamentType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@RestController
@RequestMapping("/profile")
class ProfileController(
    private val sessionContext: SessionContext,
    private val leagueTableFunction: LeagueTableFunction,
    private val scheduleService: ScheduleService,
    private val leagues: ConcurrentHashMap<UUID, League>,
    private val matchesService: MatchesService,
    private val teams: ConcurrentHashMap<UUID, Club>,
    private val roundsService: RoundsService,
    private val tournamentCurrentSeasonFunction: TournamentCurrentSeasonFunction,
    private val teamService: TeamService,
) {
    @GetMapping("/coach")
    fun coach(): Coach = sessionContext.player

    @GetMapping("/league")
    fun league(): LeagueInfo? {
        if (sessionContext.club == null) return null
        if (sessionContext.club!!.tournaments[TournamentType.LEAGUE] == null) return null
        val season = sessionContext.club!!
            .tournaments[TournamentType.LEAGUE]!!
            .let { tournamentCurrentSeasonFunction.execute(it)}
        val league = leagues[season.league]!!
        return LeagueInfo(
            leagueName = league.name,
            table = leagueTableFunction.getLeagueTable(
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

    @GetMapping("/team")
    fun getTeam(): TeamInfo? {
        sessionContext.club ?: return null
        return teamService.getTeamInfo(sessionContext.club!!.id)
    }
}