package com.footballmanager.domain.calendar

import java.time.LocalDateTime

interface CalendarProvider {
    fun now(): LocalDateTime
    fun advanceOneSlot(): LocalDateTime
}
