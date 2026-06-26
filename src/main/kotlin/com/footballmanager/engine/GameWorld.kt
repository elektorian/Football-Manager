package com.footballmanager.engine

import com.footballmanager.domain.Club
import com.footballmanager.domain.Competition
import com.footballmanager.domain.GameSession

class GameWorld(
    initialDate: java.time.LocalDate
) {
    var clock: GameClock = GameClock(initialDate)
        internal set

    val calendar: EventCalendar = EventCalendar()

    var session: GameSession? = null
    val clubs: MutableMap<Long, Club> = mutableMapOf()
    val competitions: MutableMap<Long, Competition> = mutableMapOf()
}
