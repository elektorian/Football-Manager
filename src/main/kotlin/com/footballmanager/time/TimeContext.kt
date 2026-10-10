package com.footballmanager.time

import com.footballmanager.time.enumeration.TimePosition
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime

@Component
class TimeContext {
    @Volatile
    private lateinit var timestamp: LocalDateTime

    @PostConstruct
    fun init() {
        timestamp = LocalDateTime.of(2019, 7, 1, 8, 0)
    }

    fun getTimestamp(): LocalDateTime = timestamp

    fun setCurrentTime(newTime: LocalDateTime) {
        timestamp = newTime
    }

    fun getDate(): LocalDate = timestamp.toLocalDate()

    fun isHour(timePosition: TimePosition): Boolean {
        return timePosition.hour == timestamp.hour
    }
}