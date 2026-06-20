package com.footballmanager.calendar

import com.footballmanager.calendar.dto.AdvanceResultDto
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/calendar")
class CalendarController(
    private val advanceCalendarUseCase: AdvanceCalendarUseCase,
) {
    @PostMapping("/advance")
    fun advance(): AdvanceResultDto = advanceCalendarUseCase.execute()
}
