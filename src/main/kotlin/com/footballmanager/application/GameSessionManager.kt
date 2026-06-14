package com.footballmanager.application

import com.footballmanager.domain.model.*
import com.footballmanager.domain.service.LeagueService
import org.springframework.stereotype.Service
import java.time.LocalDate
import kotlin.random.Random

@Service
class GameSessionManager {
    private var _activeState: GameState = GameState()
    val activeState: GameState 
        get() = _activeState

    private val leagueService = LeagueService()

    init {
        // Initialize RPL by default for now
        val (league, matches) = leagueService.generateRPL()
        _activeState = GameState(
            meta = GameMeta(),
            leagues = listOf(league),
            teams = generateTeamListForLeague(league), 
            matches = matches
        )
    }

    private fun generateTeamListForLeague(league: League): List<Team> {
        val teamNames = LeagueService.DEFAULT_TEAM_NAMES
        return teamNames.mapIndexed { index, name -> Team(index.toString(), name) }
    }

    fun updateState(newState: GameState) {
        this._activeState = newState
    }

    // Business Logic for time progression
    fun advanceTime() {
        val oldDate = _activeState.meta.currentDate
        val newDate = oldDate.plusDays(1)
        _activeState.meta.currentDate = newDate

        // Check if it's Sunday to simulate games
        if (newDate.dayOfWeek == java.time.DayOfWeek.SUNDAY) {
            simulateSundayMatches(newDate)
        }
    }

    private fun simulateSundayMatches(date: LocalDate) {
        _activeState.matches.filter { it.scheduledDate == date }.forEach { match ->
            match.result = MatchResult(Random.nextInt(0, 5), Random.nextInt(0, 5))
        }
    }

    fun getCurrentDate(): LocalDate = activeState.meta.currentDate
}

data class GameState(
    var meta: GameMeta = GameMeta(),
    val leagues: List<League> = emptyList(),
    val teams: List<Team> = emptyList(),
    val matches: List<Match> = emptyList()
)