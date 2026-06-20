package com.footballmanager.calendar

import com.footballmanager.application.events.NewDayEvent
import com.footballmanager.entities.Club
import com.footballmanager.entities.match.Match
import com.footballmanager.events.EventsEngine
import com.footballmanager.functions.TournamentTodayMatchesFunction
import com.footballmanager.matches.MatchesEngine
import com.footballmanager.notifications.NotificationsService
import com.footballmanager.notifications.model.Notification
import com.footballmanager.notifications.model.NotificationType
import com.footballmanager.notifications.payload.MatchPreviewPayload
import com.footballmanager.notifications.payload.RoundPreviewPayloadGenerator
import com.footballmanager.seasons.SeasonService
import com.footballmanager.session.SessionContext
import com.footballmanager.tournaments.enumerations.TournamentType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.springframework.context.ApplicationEventPublisher
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import kotlin.test.assertEquals

class CalendarEngineTest {
    private lateinit var eventsEngine: EventsEngine
    private lateinit var matchesEngine: MatchesEngine
    private lateinit var notificationsService: NotificationsService
    private lateinit var currentMomentHolder: CurrentMomentHolder

    @BeforeEach
    fun setUp() {
        eventsEngine = mock(EventsEngine::class.java)
        matchesEngine = mock(MatchesEngine::class.java)
        notificationsService = mock(NotificationsService::class.java)
        currentMomentHolder = CurrentMomentHolder(mock(ApplicationEventPublisher::class.java))
        `when`(notificationsService.isEmpty()).thenReturn(true)
    }

    private fun createEngine() = CalendarEngine(
        eventsEngine, matchesEngine, notificationsService, currentMomentHolder,
    )

    @Test
    @DisplayName("Блокировка: непустые нотификации — advance возвращает ту же дату")
    fun `advance blocked by notifications`() {
        `when`(notificationsService.isEmpty()).thenReturn(false)
        val engine = createEngine()

        val result = engine.advance()

        assertEquals("2020-07-01T08:00", result)
        verify(eventsEngine, never()).process()
        verify(matchesEngine, never()).process()
    }

    @Test
    @DisplayName("Пустые нотификации — advance со среды до понедельника")
    fun `advance from default start goes to monday`() {
        val engine = createEngine()

        val result = engine.advance()

        assertEquals("2020-07-06T08:00", result)
        verify(eventsEngine, times(5)).process()
        verify(matchesEngine, times(5)).process()
    }

    @Test
    @DisplayName("Несколько sequential advance — каждый раз +1 неделя")
    fun `sequential advances each go to next monday`() {
        val engine = createEngine()

        assertEquals("2020-07-06T08:00", engine.advance())
        assertEquals("2020-07-13T08:00", engine.advance())
        assertEquals("2020-07-20T08:00", engine.advance())
        assertEquals("2020-07-27T08:00", engine.advance())
    }

    @Test
    @DisplayName("Понедельник 08:00 обрабатывается (не пропускается)")
    fun `monday morning is not skipped`() {
        currentMomentHolder.set(LocalDateTime.of(LocalDate.of(2020, 7, 6), LocalTime.of(8, 0)))
        val engine = createEngine()

        val result = engine.advance()

        assertEquals("2020-07-13T08:00", result)
        verify(eventsEngine, atLeastOnce()).process()
    }

    @Test
    @DisplayName("END_HOUR в воскресенье — останов на понедельнике без обработки")
    fun `sunday end stops at monday without processing`() {
        currentMomentHolder.set(LocalDateTime.of(LocalDate.of(2020, 7, 5), LocalTime.of(22, 0)))
        val engine = createEngine()

        val result = engine.advance()

        assertEquals("2020-07-06T08:00", result)
        verify(eventsEngine, never()).process()
        verify(matchesEngine, never()).process()
    }

    @Test
    @DisplayName("Граница месяца: июль → август")
    fun `month boundary july to august`() {
        currentMomentHolder.set(LocalDateTime.of(LocalDate.of(2020, 7, 31), LocalTime.of(22, 0)))
        val engine = createEngine()

        val result = engine.advance()

        assertEquals("2020-08-03T08:00", result)
    }

    @Test
    @DisplayName("Граница года: 2020 → 2021")
    fun `year boundary`() {
        currentMomentHolder.set(LocalDateTime.of(LocalDate.of(2020, 12, 31), LocalTime.of(22, 0)))
        val engine = createEngine()

        val result = engine.advance()

        assertEquals("2021-01-04T08:00", result)
    }

    @Test
    @DisplayName("Нотификация, добавленная eventsEngine, останавливает рекурсию")
    fun `notification added by events stops recursion`() {
        `when`(notificationsService.isEmpty()).thenReturn(true, false)
        val engine = createEngine()

        val result = engine.advance()

        assertEquals("2020-07-01T16:00", result)
        verify(eventsEngine, times(1)).process()
        verify(matchesEngine, never()).process()
    }

    @Test
    @DisplayName("Очистка нотификации — advance продолжает с того же места")
    fun `after clearing notifications advance continues from where it stopped`() {
        `when`(notificationsService.isEmpty()).thenReturn(true, false)
        val engine = createEngine()

        assertEquals("2020-07-01T16:00", engine.advance())

        `when`(notificationsService.isEmpty()).thenReturn(true)
        val result = engine.advance()

        assertEquals("2020-07-06T08:00", result)
        verify(matchesEngine, atLeastOnce()).process()
    }

    @Test
    @DisplayName("Когда есть матчи сегодня — создаётся нотификация")
    fun `notification generated when matches exist today`() {
        val tournamentId = UUID.randomUUID()
        val club = mock(Club::class.java)
        val matches = listOf(mock(Match::class.java))
        val notification = Notification(title = "t", type = NotificationType.MATCH_PREVIEW, payload = MatchPreviewPayload("", emptyList()), timestamp = LocalDateTime.now(), date = LocalDate.now())
        val tournamentTodayMatchesFunction = mock(TournamentTodayMatchesFunction::class.java)
        val roundPreviewPayloadGenerator = mock(RoundPreviewPayloadGenerator::class.java)
        val sessionContext = SessionContext(
            ConcurrentHashMap(),
            mock(SeasonService::class.java),
            ConcurrentHashMap(),
        )

        `when`(club.tournaments).thenReturn(ConcurrentHashMap(mapOf(TournamentType.LEAGUE to tournamentId)))
        `when`(tournamentTodayMatchesFunction.execute(tournamentId)).thenReturn(matches)
        `when`(roundPreviewPayloadGenerator.generate(tournamentId)).thenReturn(notification)

        sessionContext.club = club
        val processor = DailyNotificationsProcessor(
            sessionContext, tournamentTodayMatchesFunction, roundPreviewPayloadGenerator, notificationsService,
        )

        processor.onNewDay(NewDayEvent)

        verify(tournamentTodayMatchesFunction).execute(tournamentId)
        verify(roundPreviewPayloadGenerator).generate(tournamentId)
        verify(notificationsService).create(notification)
    }
}
