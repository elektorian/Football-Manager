package com.footballmanager.calendar

import com.footballmanager.application.repository.NotificationRepository
import com.footballmanager.events.EventsEngine
import com.footballmanager.matches.MatchesEngine
import com.footballmanager.notifications.NotificationsService
import org.springframework.stereotype.Component
import java.time.DayOfWeek
import java.time.temporal.ChronoUnit

@Component
class CalendarEngine(
    private val eventsEngine: EventsEngine,
    private val matchesEngine: MatchesEngine,
    private val currentMomentHolder: CurrentMomentHolder,
    private val notificationRepository: NotificationRepository,
) {
    @Synchronized
    fun advance(): String {
        if (!notificationRepository.isEmpty()) return currentMomentHolder.get().toString()

        when (DayHour.getByValue(currentMomentHolder.get().hour)) {
            DayHour.START_HOUR -> {
                eventsEngine.process()
                currentMomentHolder.setHour(DayHour.MATCH_HOUR)
            }

            DayHour.MATCH_HOUR -> {
                matchesEngine.process()
                currentMomentHolder.setHour(DayHour.END_HOUR)
            }

            DayHour.END_HOUR -> {
                currentMomentHolder.set(
                    currentMomentHolder.get()
                        .plusDays(1)
                        .truncatedTo(ChronoUnit.DAYS)
                        .withHour(DayHour.START_HOUR.value)
                )
                if (currentMomentHolder.get().dayOfWeek == DayOfWeek.MONDAY) return currentMomentHolder.get().toString()
            }
        }
        return advance()
    }
}
