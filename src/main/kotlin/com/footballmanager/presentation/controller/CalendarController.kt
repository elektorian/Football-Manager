package com.footballmanager.presentation.controller

import com.footballmanager.application.dto.AdvanceResultDto
import com.footballmanager.application.port.input.CalendarUseCase
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/calendar")
class CalendarController(
    private val calendarUseCase: CalendarUseCase,
) {
    @PostMapping("/advance")
    fun advance(): AdvanceResultDto = calendarUseCase.advance()
}
