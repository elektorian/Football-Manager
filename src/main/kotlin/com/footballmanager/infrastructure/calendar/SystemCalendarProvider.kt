package com.footballmanager.infrastructure.calendar

import com.footballmanager.domain.calendar.CalendarProvider
import com.footballmanager.domain.calendar.TimeSlot
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class SystemCalendarProvider : CalendarProvider {

    private var current: LocalDateTime = LocalDateTime.of(2019, 7, 8, 8, 0)

    override fun now(): LocalDateTime = current

    override fun advanceOneSlot(): LocalDateTime {
        val day = current.toLocalDate()
        val currentTime = current.toLocalTime()

        val slot = TimeSlot.entries.find { it.time == currentTime }
            ?: TimeSlot.Start

        val nextSlot = slot.next()

        if (nextSlot == TimeSlot.Start) {
            current = LocalDateTime.of(day.plusDays(1), TimeSlot.Start.time)
        } else {
            current = LocalDateTime.of(day, nextSlot.time)
        }

        return current
    }
}
