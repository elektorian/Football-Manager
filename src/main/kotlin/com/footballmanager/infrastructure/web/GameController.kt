package com.footballmanager.infrastructure.web

import com.footballmanager.application.GameSessionManager
import com.footballmanager.application.SaveLoadService
import com.footballmanager.domain.model.*
import com.footballmanager.domain.service.LeagueService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/game")
class GameController(
    private val session: GameSessionManager,
    private val saveLoadService: SaveLoadService
) {
    private val leagueService = LeagueService()

    @GetMapping("/state")
    fun getState() = mapOf(
        "currentDate" to session.activeState.meta.currentDate.toString(),
        "gameName" to session.activeState.meta.gameName
    )

    @PostMapping("/next")
    fun nextStop(): String {
        session.advanceTime()
        return session.activeState.meta.currentDate.toString()
    }

    @GetMapping("/tournaments")
    fun getTournaments() = session.activeState.leagues.map { league ->
        val standings = leagueService.calculateStandings(league.id, session.activeState.matches, session.activeState.teams)
        mapOf(
            "id" to league.id,
            "name" to league.name,
            "topStandings" to standings.take(5).map { it }
        )
    }

    @GetMapping("/leagues/{id}")
    fun getLeague(@PathVariable id: String) = mapOf(
        "league" to session.activeState.leagues.find { it.id == id },
        "standings" to leagueService.calculateStandings(
            session.activeState.leagues.find { it.id == id }?.id ?: "", 
            session.activeState.matches, 
            session.activeState.teams
        )
    )

    @GetMapping("/leagues/{id}/matches")
    fun getLeagueMatches(@PathVariable id: String) = session.activeState.matches
        .filter { it.leagueId == id }
        .groupBy { it.round }
        .toSortedMap()
        .mapValues { entry -> entry.value.map { match -> 
            mapOf(
                "id" to match.id,
                "teamA" to session.activeState.teams.find { it.id == match.teamAId }?.name,
                "teamB" to session.activeState.teams.find { it.id == match.teamBId }?.name,
                "date" to match.scheduledDate.toString(),
                "result" to match.result
            )
        }}

    @GetMapping("/matches/{id}")
    fun getMatch(@PathVariable id: String) = session.activeState.matches.find { it.id == id }?.let { match ->
        mapOf(
            "teamA" to session.activeState.teams.find { it.id == match.teamAId }?.name,
            "teamB" to session.activeState.teams.find { it.id == match.teamBId }?.name,
            "result" to match.result,
            "date" to match.scheduledDate.toString()
        )
    } ?: mapOf("error" to "Match not found")

    @PostMapping("/save")
    fun save(@RequestBody request: SaveRequest): String {
        saveLoadService.saveGame(request.saveName, session.activeState)
        return "Saved successfully as ${request.saveName}"
    }

    @PostMapping("/load")
    fun load(@RequestBody request: LoadRequest): String {
        val loadedState = saveLoadService.loadGame(request.saveName)
        return if (loadedState != null) {
            session.updateState(loadedState)
            "Loaded ${request.saveName} successfully"
        } else {
            "Save file not found"
        }
    }
}

data class SaveRequest(val saveName: String)
data class LoadRequest(val saveName: String)
