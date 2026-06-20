package com.footballmanager.domain.service

import com.footballmanager.domain.model.DayHour
import org.springframework.stereotype.Component
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Component
class CalendarEngine {
    @Synchronized
    fun advance(
        currentMoment: LocalDateTime,
        hasUnreadNotifications: Boolean,
        processMatches: () -> Unit,
        onNewDay: () -> Unit,
    ): LocalDateTime {
        if (hasUnreadNotifications) return currentMoment

        return when (DayHour.getByValue(currentMoment.hour)) {
            DayHour.START_HOUR -> currentMoment.withHour(DayHour.MATCH_HOUR.value)
            DayHour.MATCH_HOUR -> {
                processMatches()
                currentMoment.withHour(DayHour.END_HOUR.value)
            }
            DayHour.END_HOUR -> {
                val nextDay = currentMoment
                    .plusDays(1)
                    .truncatedTo(ChronoUnit.DAYS)
                    .withHour(DayHour.START_HOUR.value)
                if (nextDay.dayOfWeek == DayOfWeek.MONDAY) {
                    onNewDay()
                }
                nextDay
            }
        }
    }
}
