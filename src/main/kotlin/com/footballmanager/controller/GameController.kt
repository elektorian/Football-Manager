package com.footballmanager.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/game")
class GameController {

    @GetMapping("/state")
    fun state(): ResponseEntity<Map<String, Any>> {
        return ResponseEntity.ok(mapOf(
            "currentDate" to "15.06.2026",
            "currentMonth" to "Июнь 2026",
            "currentDay" to 15,
            "season" to "2026"
        ))
    }
}
