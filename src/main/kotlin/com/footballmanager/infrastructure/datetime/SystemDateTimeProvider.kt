package com.footballmanager.infrastructure.datetime

import com.footballmanager.domain.datetime.DateTimeProvider
import com.footballmanager.domain.datetime.TimeSlot
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class SystemDateTimeProvider : DateTimeProvider {

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
