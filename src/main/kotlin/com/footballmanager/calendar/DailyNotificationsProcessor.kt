package com.footballmanager.calendar

import com.footballmanager.application.events.NewDayEvent
import com.footballmanager.functions.TournamentTodayMatchesFunction
import com.footballmanager.notifications.NotificationsService
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
    private val notificationsService: NotificationsService,
) {
    @EventListener
    fun onNewDay(event: NewDayEvent) {
        checkTodayMatchesNotification()
    }

    private fun checkTodayMatchesNotification(): Boolean {
        sessionContext.club?.tournaments?.forEach { (_, tournamentId) ->
            val todayMatches = tournamentTodayMatchesFunction.execute(tournamentId)
            if (!todayMatches.isNullOrEmpty()) {
                notificationsService.create(roundPreviewPayloadGenerator.generate(tournamentId))
                return true
            }
        }
        return false
    }
}