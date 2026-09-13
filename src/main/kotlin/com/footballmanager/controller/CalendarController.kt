package com.footballmanager.controller

import com.footballmanager.application.calendar.CalendarService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping

@Controller
class CalendarController(private val calendarService: CalendarService) {

    @PostMapping("/game/advance")
    fun advance(): String {
        calendarService.advanceOneSlot()
        return "redirect:/"
    }
}
