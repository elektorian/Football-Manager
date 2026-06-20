package com.footballmanager.domain.service

import com.footballmanager.domain.model.DayHour
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CalendarEngineTest {
    private val engine = CalendarEngine()

    @Test
    @DisplayName("Блокировка: непустые нотификации — advance возвращает ту же дату")
    fun `blocked by unread notifications`() {
        var matchesProcessed = false
        var newDayCalled = false

        val result = engine.advance(
            currentMoment = LocalDateTime.of(2020, 7, 1, 8, 0),
            hasUnreadNotifications = true,
            processMatches = { matchesProcessed = true },
            onNewDay = { newDayCalled = true },
        )

        assertEquals(LocalDateTime.of(2020, 7, 1, 8, 0), result)
        assertFalse(matchesProcessed)
        assertFalse(newDayCalled)
    }

    @Test
    @DisplayName("START_HOUR (8:00) → MATCH_HOUR (16:00)")
    fun `start hour advances to match hour`() {
        var matchesProcessed = false
        var newDayCalled = false

        val result = engine.advance(
            currentMoment = LocalDateTime.of(2020, 7, 1, 8, 0),
            hasUnreadNotifications = false,
            processMatches = { matchesProcessed = true },
            onNewDay = { newDayCalled = true },
        )

        assertEquals(LocalDateTime.of(2020, 7, 1, 16, 0), result)
        assertFalse(matchesProcessed)
        assertFalse(newDayCalled)
    }

    @Test
    @DisplayName("MATCH_HOUR (16:00) → END_HOUR (22:00) с обработкой матчей")
    fun `match hour processes matches and advances to end hour`() {
        var matchesProcessed = false
        var newDayCalled = false

        val result = engine.advance(
            currentMoment = LocalDateTime.of(2020, 7, 1, 16, 0),
            hasUnreadNotifications = false,
            processMatches = { matchesProcessed = true },
            onNewDay = { newDayCalled = true },
        )

        assertEquals(LocalDateTime.of(2020, 7, 1, 22, 0), result)
        assertTrue(matchesProcessed)
        assertFalse(newDayCalled)
    }

    @Test
    @DisplayName("END_HOUR (22:00) — следующий день START_HOUR (8:00)")
    fun `end hour advances to next day start hour`() {
        var matchesProcessed = false
        var newDayCalled = false

        val result = engine.advance(
            currentMoment = LocalDateTime.of(2020, 7, 1, 22, 0),
            hasUnreadNotifications = false,
            processMatches = { matchesProcessed = true },
            onNewDay = { newDayCalled = true },
        )

        assertEquals(LocalDateTime.of(2020, 7, 2, 8, 0), result)
        assertFalse(matchesProcessed)
        assertFalse(newDayCalled)
    }

    @Test
    @DisplayName("END_HOUR в воскресенье → понедельник START_HOUR с вызовом onNewDay")
    fun `sunday end hour advances to monday and calls onNewDay`() {
        var matchesProcessed = false
        var newDayCalled = false

        val result = engine.advance(
            currentMoment = LocalDateTime.of(2020, 7, 5, 22, 0),
            hasUnreadNotifications = false,
            processMatches = { matchesProcessed = true },
            onNewDay = { newDayCalled = true },
        )

        assertEquals(LocalDateTime.of(2020, 7, 6, 8, 0), result)
        assertFalse(matchesProcessed)
        assertTrue(newDayCalled)
    }

    @Test
    @DisplayName("Полный цикл: START → MATCH → END → следующий день")
    fun `full day cycle`() {
        var matchesProcessed = false

        val afterStart = engine.advance(
            currentMoment = LocalDateTime.of(2020, 7, 1, 8, 0),
            hasUnreadNotifications = false,
            processMatches = { matchesProcessed = true },
            onNewDay = {},
        )
        assertEquals(LocalDateTime.of(2020, 7, 1, 16, 0), afterStart)

        val afterMatch = engine.advance(
            currentMoment = afterStart,
            hasUnreadNotifications = false,
            processMatches = { matchesProcessed = true },
            onNewDay = {},
        )
        assertEquals(LocalDateTime.of(2020, 7, 1, 22, 0), afterMatch)
        assertTrue(matchesProcessed)

        matchesProcessed = false
        val afterEnd = engine.advance(
            currentMoment = afterMatch,
            hasUnreadNotifications = false,
            processMatches = { matchesProcessed = true },
            onNewDay = {},
        )
        assertEquals(LocalDateTime.of(2020, 7, 2, 8, 0), afterEnd)
        assertFalse(matchesProcessed)
    }

    @Test
    @DisplayName("Граница месяца: июль → август")
    fun `month boundary`() {
        val result = engine.advance(
            currentMoment = LocalDateTime.of(2020, 7, 31, 22, 0),
            hasUnreadNotifications = false,
            processMatches = {},
            onNewDay = {},
        )

        assertEquals(LocalDateTime.of(2020, 8, 1, 8, 0), result)
    }

    @Test
    @DisplayName("Граница года: 2020 → 2021")
    fun `year boundary`() {
        val result = engine.advance(
            currentMoment = LocalDateTime.of(2020, 12, 31, 22, 0),
            hasUnreadNotifications = false,
            processMatches = {},
            onNewDay = {},
        )

        assertEquals(LocalDateTime.of(2021, 1, 1, 8, 0), result)
    }
}
