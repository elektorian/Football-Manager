package com.footballmanager.engine

import java.time.LocalDate

class EventCalendar {
    private val scheduled: MutableMap<LocalDate, MutableList<ScheduledEvent>> = mutableMapOf()

    fun schedule(event: ScheduledEvent) {
        scheduled.getOrPut(event.date) { mutableListOf() }.add(event)
    }

    fun eventsFor(date: LocalDate): List<ScheduledEvent> {
        return scheduled.remove(date) ?: emptyList()
    }

    fun hasEventsOn(date: LocalDate): Boolean {
        return scheduled.containsKey(date)
    }

    fun clear() {
        scheduled.clear()
    }
}
