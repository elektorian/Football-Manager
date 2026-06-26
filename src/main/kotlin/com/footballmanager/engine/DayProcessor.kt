package com.footballmanager.engine

import java.time.LocalDate

interface DayProcessor {
    val priority: Int

    fun process(
        date: LocalDate,
        scheduled: List<ScheduledEvent>,
        world: GameWorld
    ): List<GameEvent>
}
