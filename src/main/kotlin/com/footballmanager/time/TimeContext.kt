package com.footballmanager.time

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
}