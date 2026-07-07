package com.footballmanager.domain.core.calendar

import com.footballmanager.domain.dao.NotificationRepository
import com.footballmanager.representation.panel.common.advance.dto.AdvanceResultDto
import org.springframework.stereotype.Component

@Component
class CalendarService(
    private val calendarEngine: CalendarEngine,
    private val notificationRepository: NotificationRepository,
) {
    fun advance(): AdvanceResultDto {
        val currentMoment = calendarEngine.advance()
        return AdvanceResultDto(
            currentMoment = currentMoment,
            anyUnreadNotifications = !notificationRepository.isEmpty(),
        )
    }
}
