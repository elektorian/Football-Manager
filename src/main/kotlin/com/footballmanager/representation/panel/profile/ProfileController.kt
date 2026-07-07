package com.footballmanager.representation.panel.profile

import com.footballmanager.domain.core.person.model.Coach
import com.footballmanager.domain.core.team.TeamService
import com.footballmanager.domain.core.tournament.enumerations.TournamentType
import com.footballmanager.domain.dao.*
import com.footballmanager.domain.functions.LeagueTableFunction
import com.footballmanager.domain.functions.TournamentCurrentSeasonFunction
import com.footballmanager.domain.session.SessionContext
import com.footballmanager.representation.domain.team.dto.TeamInfo
import com.footballmanager.representation.domain.tournament.dto.LeagueInfo
import com.footballmanager.representation.domain.tournament.dto.MatchInfo
import com.footballmanager.representation.domain.tournament.dto.RoundInfo
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/profile")
class ProfileController(
    private val sessionContext: SessionContext,
    private val leagueTableFunction: LeagueTableFunction,
    private val matchRepository: MatchRepository,
    private val tournamentRepository: TournamentRepository,
    private val teamRepository: TeamRepository,
    private val roundRepository: RoundRepository,
    private val tournamentCurrentSeasonFunction: TournamentCurrentSeasonFunction,
    private val teamService: TeamService,
    private val scheduleRepository: ScheduleRepository,
) {
    @GetMapping("/coach")
    fun coach(): Coach = sessionContext.player

    @GetMapping("/league")
    fun league(): LeagueInfo? {
        if (sessionContext.team == null) return null
        if (sessionContext.team!!.tournaments[TournamentType.LEAGUE] == null) return null
        val season = sessionContext.team!!
            .tournaments[TournamentType.LEAGUE]!!
            .let { tournamentCurrentSeasonFunction.execute(it)}
        val league = tournamentRepository.get(season.league)
        return LeagueInfo(
            leagueId = league.id,
            leagueName = league.name,
            table = leagueTableFunction.getLeagueTable(
                leagueId = league.id,
                seasonId = season.id,
            ),
            rounds = season.schedule?.let { scheduleRepository.get(it) }?.rounds?.map { roundId ->
                val round = roundRepository.get(roundId)
                RoundInfo(
                    number = round.number,
                    passed = round.passed,
                    matches = round.matches.map { matchRepository.get(it) }.map { match ->
                        MatchInfo(
                            id = match.id,
                            date = match.date,
                            homeTeamId = match.homeTeam,
                            homeTeamName = teamRepository.get(match.homeTeam).name,
                            homeTeamScore = match.homeTeamResult?.scored?.toString() ?: "-",
                            awayTeamId = match.awayTeam,
                            awayTeamName = teamRepository.get(match.awayTeam).name,
                            awayTeamScore = match.awayTeamResult?.scored?.toString() ?: "-",
                        )
                    }
                )
            },
        )
    }

    @GetMapping("/team")
    fun getTeam(): TeamInfo? {
        sessionContext.team ?: return null
        return teamService.getTeamInfo(sessionContext.team!!.id)
    }

    @GetMapping("/schedule")
    fun schedule(): List<MatchInfo> {
        val team = sessionContext.team ?: return emptyList()
        return teamService.getTeamSchedule(team.id)
    }
}