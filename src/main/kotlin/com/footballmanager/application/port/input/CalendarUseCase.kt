package com.footballmanager.application.port.input

import com.footballmanager.application.dto.AdvanceResultDto

interface CalendarUseCase {
    fun advance(): AdvanceResultDto
}
