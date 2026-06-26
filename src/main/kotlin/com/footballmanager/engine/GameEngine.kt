package com.footballmanager.engine

import java.time.LocalDate

class GameEngine(
    val world: GameWorld,
    private val processors: List<DayProcessor>
) {
    private val sortedProcessors: List<DayProcessor> =
        processors.sortedBy { it.priority }

    fun advance(days: Int): List<GameEvent> {
        require(days > 0) { "Must advance by at least 1 day, got $days" }

        val producedEvents = mutableListOf<GameEvent>()

        repeat(days) {
            val date: LocalDate = tick()
            producedEvents.addAll(processDate(date))
        }

        return producedEvents
    }

    fun advanceUntil(date: LocalDate): List<GameEvent> {
        val today = world.clock.currentDate
        require(!date.isBefore(today)) { "Target date $date is before current date $today" }

        val daysBetween = java.time.temporal.ChronoUnit.DAYS.between(today, date)
        if (daysBetween == 0L) return emptyList()

        return advance(daysBetween.toInt())
    }

    private fun tick(): LocalDate {
        world.clock = world.clock.advance(1)
        return world.clock.currentDate
    }

    private fun processDate(date: LocalDate): List<GameEvent> {
        val scheduled = world.calendar.eventsFor(date)
        val dayEvents = mutableListOf<GameEvent>()

        for (processor in sortedProcessors) {
            dayEvents.addAll(processor.process(date, scheduled, world))
        }

        return dayEvents
    }
}
