package com.footballmanager.domain.core.calendar

import com.footballmanager.domain.core.calendar.event.NewDayEvent
import com.footballmanager.domain.core.notification.payload.roundPreview.RoundPreviewPayloadGenerator
import com.footballmanager.domain.dao.NotificationRepository
import com.footballmanager.domain.functions.TournamentTodayMatchesFunction
import com.footballmanager.domain.session.SessionContext
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import kotlin.collections.isNullOrEmpty

@Component
class NewDayProcessor(
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