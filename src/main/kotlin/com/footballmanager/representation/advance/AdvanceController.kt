package com.footballmanager.representation.advance

import com.footballmanager.calendar.AdvanceCalendarUseCase
import com.footballmanager.representation.advance.dto.AdvanceResultDto
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AdvanceController(
    private val advanceCalendarUseCase: AdvanceCalendarUseCase,
) {
    @PostMapping("/calendar/advance")
    fun advance(): AdvanceResultDto = advanceCalendarUseCase.execute()
}