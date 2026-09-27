package com.footballmanager.time

import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class TimeContext {
    @Volatile
    private lateinit var time: LocalDateTime

    @PostConstruct
    fun init() {
        time = LocalDateTime.of(2019, 7, 1, 8, 0)
    }

    fun getCurrentTime(): LocalDateTime = time

    fun setCurrentTime(newTime: LocalDateTime) {
        time = newTime
    }
}