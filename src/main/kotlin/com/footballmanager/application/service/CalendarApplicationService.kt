package com.footballmanager.application.service

import com.footballmanager.application.dto.AdvanceResultDto
import com.footballmanager.application.port.input.CalendarUseCase
import com.footballmanager.domain.model.Match
import com.footballmanager.domain.repository.LeagueRepository
import com.footballmanager.domain.repository.MatchRepository
import com.footballmanager.domain.repository.NotificationRepository
import com.footballmanager.domain.repository.RoundRepository
import com.footballmanager.domain.repository.ScheduleRepository
import com.footballmanager.domain.repository.SeasonRepository
import com.footballmanager.domain.service.CalendarEngine
import com.footballmanager.domain.service.MatchesEngine
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.UUID

@Component
class CalendarApplicationService(
    private val calendarEngine: CalendarEngine,
    private val matchesEngine: MatchesEngine,
    private val matchRepository: MatchRepository,
    private val roundRepository: RoundRepository,
    private val seasonRepository: SeasonRepository,
    private val leagueRepository: LeagueRepository,
    private val scheduleRepository: ScheduleRepository,
    private val notificationRepository: NotificationRepository,
) : CalendarUseCase {

    private var currentMoment: LocalDateTime = LocalDateTime.of(2020, 7, 1, 8, 0)

    @Synchronized
    fun getCurrentMoment(): LocalDateTime = currentMoment

    @Synchronized
    fun setCurrentMoment(moment: LocalDateTime) {
        if (moment == currentMoment) return
        if (moment < currentMoment) throw IllegalStateException("Время не может двигаться взад")
        currentMoment = moment
    }

    override fun advance(): AdvanceResultDto {
        val moment = advanceCalendar()
        return AdvanceResultDto(
            currentMoment = moment.toString(),
            anyUnreadNotifications = !notificationRepository.findAll().all { it.checked },
        )
    }

    private fun advanceCalendar(): LocalDateTime {
        val hasUnread = !notificationRepository.findAll().all { it.checked }
        val newMoment = calendarEngine.advance(
            currentMoment = currentMoment,
            hasUnreadNotifications = hasUnread,
            processMatches = { processAllMatches() },
            onNewDay = { },
        )
        setCurrentMoment(newMoment)
        return newMoment
    }

    private fun processAllMatches() {
        for (league in leagueRepository.findAll()) {
            val todayMatches = findTodayMatches(league.id) ?: continue
            todayMatches.forEach { matchesEngine.playMatch(it) }
            val currentRound = findCurrentRound(league.id)
                ?: throw IllegalStateException("Тур должен быть когда играются его матчи")
            if (currentRound.matches.all { mid -> matchRepository.findById(mid)?.passed() == true }) {
                roundRepository.save(currentRound.copy(passed = true))
            }
        }
    }

    private fun findTodayMatches(leagueId: UUID): List<Match>? {
        val currentRound = findCurrentRound(leagueId) ?: return null
        val currentDate = currentMoment.toLocalDate()
        return currentRound.matches
            .mapNotNull { matchRepository.findById(it) }
            .filter { it.date == currentDate }
            .ifEmpty { null }
    }

    private fun findCurrentRound(leagueId: UUID): com.footballmanager.domain.model.Round? {
        val seasons = seasonRepository.findByIds(
            leagueRepository.findById(leagueId)?.seasons?.toList() ?: return null
        )
        val latestSeason = seasons.maxByOrNull { it.year } ?: return null
        val scheduleId = latestSeason.schedule ?: return null
        val schedule = scheduleRepository.findById(scheduleId) ?: return null
        return schedule.rounds
            .mapNotNull { roundRepository.findById(it) }
            .sortedBy { it.number }
            .find { !it.passed }
    }
}

