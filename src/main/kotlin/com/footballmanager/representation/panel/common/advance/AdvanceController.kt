package com.footballmanager.representation.panel.common.advance

import com.footballmanager.domain.core.calendar.CalendarService
import com.footballmanager.representation.panel.common.advance.dto.AdvanceResultDto
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AdvanceController(
    private val calendarService: CalendarService,
) {
    @PostMapping("/calendar/advance")
    fun advance(): AdvanceResultDto = calendarService.advance()
}