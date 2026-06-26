package com.footballmanager.engine

class GameWorld(
    initialDate: java.time.LocalDate
) {
    var clock: GameClock = GameClock(initialDate)
        internal set

    val calendar: EventCalendar = EventCalendar()
}
