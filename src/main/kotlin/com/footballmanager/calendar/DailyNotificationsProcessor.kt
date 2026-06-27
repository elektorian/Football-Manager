package com.footballmanager.calendar

import com.footballmanager.application.events.NewDayEvent
import com.footballmanager.domain.repository.NotificationRepository
import com.footballmanager.functions.TournamentTodayMatchesFunction
import com.footballmanager.notifications.payload.RoundPreviewPayloadGenerator
import com.footballmanager.session.SessionContext
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import kotlin.collections.component1
import kotlin.collections.component2

@Component
class DailyNotificationsProcessor(
    private val sessionContext: SessionContext,
    private val tournamentTodayMatchesFunction: TournamentTodayMatchesFunction,
    private val roundPreviewPayloadGenerator: RoundPreviewPayloadGenerator,
    private val notificationRepository: NotificationRepository,
) {
    @EventListener
    fun onNewDay(event: NewDayEvent) {
        checkTodayMatchesNotification()
    }

    private fun checkTodayMatchesNotification(): Boolean {
        sessionContext.team?.tournaments?.forEach { (_, tournamentId) ->
            val todayMatches = tournamentTodayMatchesFunction.execute(tournamentId)
            if (!todayMatches.isNullOrEmpty()) {
                notificationRepository.save(roundPreviewPayloadGenerator.generate(tournamentId))
                return true
            }
        }
        return false
    }
}