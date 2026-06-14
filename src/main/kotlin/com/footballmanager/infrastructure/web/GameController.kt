package com.footballmanager.infrastructure.web

import com.footballmanager.application.GameSessionManager
import com.footballmanager.application.SaveLoadService
import com.footballmanager.domain.service.TimeEngine
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/game")
class GameController(
    private val session: GameSessionManager,
    private val saveLoadService: SaveLoadService
) {
    @GetMapping("/state")
    fun getState() = mapOf(
        "currentDate" to session.getCurrentDate().toString(),
        "gameName" to session.activeState.gameName
    )

    @PostMapping("/next")
    fun nextStop(): String {
        val nextDate = TimeEngine.calculateNextStop(session.getCurrentDate())
        session.setCurrentDate(nextDate)
        return nextDate.toString()
    }

    @PostMapping("/save")
    fun save(@RequestBody request: SaveRequest): String {
        saveLoadService.saveGame(request.saveName, session.activeState)
        return "Saved successfully as ${request.saveName}"
    }

    @PostMapping("/load")
    fun load(@RequestBody request: LoadRequest): String {
        val loadedMeta = saveLoadService.loadGame(request.saveName)
        return if (loadedMeta != null) {
            session.updateState(loadedMeta)
            "Loaded ${request.saveName} successfully"
        } else {
            "Save file not found"
        }
    }
}

data class SaveRequest(val saveName: String)
data class LoadRequest(val saveName: String)
