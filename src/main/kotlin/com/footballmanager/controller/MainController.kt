package com.footballmanager.controller

import com.footballmanager.application.calendar.CalendarService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class MainController(private val calendarService: CalendarService) {

    @GetMapping("/")
    fun index(model: Model): String {
        val now = calendarService.now()
        model.addAttribute("currentDateTime", now)
        model.addAttribute("activePage", "main")
        return "pages/index"
    }

    @GetMapping("/incoming")
    fun incoming(model: Model): String {
        val now = calendarService.now()
        model.addAttribute("currentDateTime", now)
        model.addAttribute("activePage", "incoming")
        return "pages/incoming"
    }

    @GetMapping("/squad")
    fun squad(model: Model): String {
        val now = calendarService.now()
        model.addAttribute("currentDateTime", now)
        model.addAttribute("activePage", "squad")
        return "pages/squad"
    }

    @GetMapping("/tactic")
    fun tactic(model: Model): String {
        val now = calendarService.now()
        model.addAttribute("currentDateTime", now)
        model.addAttribute("activePage", "tactic")
        return "pages/tactic"
    }

    @GetMapping("/tournaments")
    fun tournaments(model: Model): String {
        val now = calendarService.now()
        model.addAttribute("currentDateTime", now)
        model.addAttribute("activePage", "tournaments")
        return "pages/tournaments"
    }

    @GetMapping("/schedule")
    fun schedule(model: Model): String {
        val now = calendarService.now()
        model.addAttribute("currentDateTime", now)
        model.addAttribute("activePage", "schedule")
        return "pages/schedule"
    }

    @GetMapping("/club")
    fun club(model: Model): String {
        val now = calendarService.now()
        model.addAttribute("currentDateTime", now)
        model.addAttribute("activePage", "club")
        return "pages/club"
    }
}
