package com.footballmanager.calendar

import com.footballmanager.domain.repository.NotificationRepository
import com.footballmanager.calendar.dto.AdvanceResultDto
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
