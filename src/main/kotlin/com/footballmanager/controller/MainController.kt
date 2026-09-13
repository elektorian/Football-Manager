package com.footballmanager.controller

import com.footballmanager.application.datetime.DateTimeService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute

@Controller
class MainController(private val dateTimeService: DateTimeService) {

    @ModelAttribute
    fun datetimeAttributes(model: Model) {
        val now = dateTimeService.now()
        model.addAttribute("currentDateTime", now)
    }

    @GetMapping("/")
    fun index(model: Model): String {
        model.addAttribute("activePage", "main")
        return "pages/index"
    }

    @GetMapping("/incoming")
    fun incoming(model: Model): String {
        model.addAttribute("activePage", "incoming")
        return "pages/incoming"
    }

    @GetMapping("/squad")
    fun squad(model: Model): String {
        model.addAttribute("activePage", "squad")
        return "pages/squad"
    }

    @GetMapping("/tactic")
    fun tactic(model: Model): String {
        model.addAttribute("activePage", "tactic")
        return "pages/tactic"
    }

    @GetMapping("/tournaments")
    fun tournaments(model: Model): String {
        model.addAttribute("activePage", "tournaments")
        return "pages/tournaments"
    }

    @GetMapping("/schedule")
    fun schedule(model: Model): String {
        model.addAttribute("activePage", "schedule")
        return "pages/schedule"
    }

    @GetMapping("/club")
    fun club(model: Model): String {
        model.addAttribute("activePage", "club")
        return "pages/club"
    }
}
