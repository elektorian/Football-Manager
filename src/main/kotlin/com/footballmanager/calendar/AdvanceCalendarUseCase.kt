package com.footballmanager.calendar

import com.footballmanager.calendar.dto.AdvanceResultDto
import com.footballmanager.notifications.NotificationsService
import org.springframework.stereotype.Component

@Component
class AdvanceCalendarUseCase(
    private val calendarEngine: CalendarEngine,
    private val notificationsService: NotificationsService,
) {
    fun execute(): AdvanceResultDto {
        val currentMoment = calendarEngine.advance()
        return AdvanceResultDto(
            currentMoment = currentMoment,
            anyUnreadNotifications = !notificationsService.isEmpty(),
        )
    }
}
