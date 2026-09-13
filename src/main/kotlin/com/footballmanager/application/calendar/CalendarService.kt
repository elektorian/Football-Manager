package com.footballmanager.application.calendar

import com.footballmanager.domain.calendar.CalendarProvider
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class CalendarService(private val calendarProvider: CalendarProvider) {

    fun now(): LocalDateTime = calendarProvider.now()

    fun advanceOneSlot(): LocalDateTime = calendarProvider.advanceOneSlot()
}
