package com.footballmanager.domain.service

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

/**
 * TimeEngine is the core clock mechanism of the simulation.
 * 
 * Business Requirements:
 * - The 'Next' button in UI must advance time to the next "stop".
 * - A stop is defined as the first day of the next week (Monday).
 * - Even if today is Monday, calling calculateNextStop should move to the FOLLOWING Monday.
 */
object TimeEngine {
    fun calculateNextStop(currentDate: LocalDate): LocalDate {
        return currentDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY))
    }
}
