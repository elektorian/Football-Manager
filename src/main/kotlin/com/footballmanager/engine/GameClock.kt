package com.footballmanager.engine

import java.time.LocalDate

data class GameClock(
    val currentDate: LocalDate
) {
    fun advance(days: Long): GameClock {
        require(days > 0) { "Must advance by at least 1 day, got $days" }
        return copy(currentDate = currentDate.plusDays(days))
    }
}
