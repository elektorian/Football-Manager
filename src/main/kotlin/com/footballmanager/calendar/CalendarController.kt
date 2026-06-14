package com.footballmanager.calendar

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/calendar")
class CalendarController(
    private val calendarEngine: CalendarEngine
) {
    @PostMapping("/advance")
    fun advance() = calendarEngine.advance()
}
