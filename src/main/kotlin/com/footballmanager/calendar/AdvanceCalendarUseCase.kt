package com.footballmanager.calendar

import com.footballmanager.application.repository.NotificationRepository
import com.footballmanager.calendar.dto.AdvanceResultDto
import com.footballmanager.notifications.NotificationsService
import org.springframework.stereotype.Component

@Component
class AdvanceCalendarUseCase(
    private val calendarEngine: CalendarEngine,
    private val notificationRepository: NotificationRepository,
) {
    fun execute(): AdvanceResultDto {
        val currentMoment = calendarEngine.advance()
        return AdvanceResultDto(
            currentMoment = currentMoment,
            anyUnreadNotifications = !notificationRepository.isEmpty(),
        )
    }
}
