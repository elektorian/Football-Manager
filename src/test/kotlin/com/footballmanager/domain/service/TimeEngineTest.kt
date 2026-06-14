package com.footballmanager.domain.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate

class TimeEngineTest {

    @Test
    fun `calculateNextStop should advance to next Monday from a Tuesday`() {
        val tuesday = LocalDate.of(2020, 7, 7) // Tuesday
        val expected = LocalDate.of(2020, 7, 13) // Next Monday
        assertEquals(expected, TimeEngine.calculateNextStop(tuesday))
    }

    @Test
    fun `calculateNextStop should advance to NEXT Monday when current date is already Monday`() {
        val monday = LocalDate.of(2020, 7, 13) // Monday
        val expected = LocalDate.of(2020, 7, 20) // Following Monday
        assertEquals(expected, TimeEngine.calculateNextStop(monday))
    }

    @Test
    fun `calculateNextStop should advance from start date`() {
        val start = LocalDate.of(2020, 7, 1) // Wednesday
        val expected = LocalDate.of(2020, 7, 6) // First Monday after start
        assertEquals(expected, TimeEngine.calculateNextStop(start))
    }
}
