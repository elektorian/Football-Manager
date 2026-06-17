package com.footballmanager.calendar

import com.footballmanager.functions.TodayMatchesFunction
import com.footballmanager.notifications.NotificationsService
import com.footballmanager.notifications.payload.RoundPreviewPayloadGenerator
import com.footballmanager.session.SessionContext
import org.springframework.stereotype.Component
import kotlin.collections.component1
import kotlin.collections.component2

@Component
class DailyNotificationsProcessor(
    private val sessionContext: SessionContext,
    private val todayMatchesFunction: TodayMatchesFunction,
    private val roundPreviewPayloadGenerator: RoundPreviewPayloadGenerator,
    private val notificationsService: NotificationsService,
) {
    fun process() {
        checkTodayMatchesNotification()
    }

    private fun checkTodayMatchesNotification(): Boolean {
        sessionContext.club?.tournaments?.forEach { (_, tournamentId) ->
            val todayMatches = todayMatchesFunction.execute(tournamentId)
            if (todayMatches.isNotEmpty()) {
                notificationsService.create(roundPreviewPayloadGenerator.generate(tournamentId))
                return true
            }
        }
        return false
    }
}