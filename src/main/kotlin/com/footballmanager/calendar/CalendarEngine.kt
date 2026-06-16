package com.footballmanager.calendar

import com.footballmanager.configuration.GlobalParameters
import com.footballmanager.events.EventsEngine
import com.footballmanager.matches.MatchesEngine
import com.footballmanager.notifications.NotificationsService
import org.springframework.stereotype.Component
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

@Component
class CalendarEngine(
    private val eventsEngine: EventsEngine,
    private val matchesEngine: MatchesEngine,
    private val notificationsService: NotificationsService,
    initialMoment: LocalDateTime = LocalDateTime.of(
        LocalDate.of(GlobalParameters.START_YEAR, 7, 1),
        LocalTime.of(8, 0)
    ),
) {
    companion object {
        private const val START_HOUR = 8
        private const val MATCH_HOUR = 16
        private const val END_HOUR = 22
    }

    private var currentMoment: LocalDateTime = initialMoment

    @Synchronized
    fun advance(): String {
        if (!notificationsService.isEmpty()) return currentMoment.toString()
        when (currentMoment.hour) {
            START_HOUR -> {
                eventsEngine.process()
                currentMoment = currentMoment.withHour(MATCH_HOUR)
            }

            MATCH_HOUR -> {
                matchesEngine.process()
                currentMoment = currentMoment.withHour(END_HOUR)
            }

            END_HOUR -> {
                currentMoment = currentMoment.plusDays(1).truncatedTo(ChronoUnit.DAYS).withHour(START_HOUR)
                if (currentMoment.dayOfWeek == DayOfWeek.MONDAY) return currentMoment.toString()
            }
        }
        return advance()
    }
}
