package com.footballmanager.application

import com.footballmanager.domain.model.GameMeta
import org.springframework.stereotype.Service

/**
 * GameSessionManager manages the currently active game state in memory.
 * 
 * Architecture Note:
 * - It acts as a singleton holding the current session's [GameMeta].
 * - Provides a way to synchronize in-memory state with persistence (Save/Load).
 */
@Service
class GameSessionManager {
    private var _activeState: GameMeta = GameMeta()
    val activeState: GameMeta 
        get() = _activeState

    fun updateState(newState: GameMeta) {
        this._activeState = newState
    }

    fun getCurrentDate(): java.time.LocalDate = activeState.currentDate

    fun setCurrentDate(date: java.time.LocalDate) {
        _activeState.currentDate = date
    }
}
