package com.footballmanager.profile

import com.footballmanager.entities.Coach
import com.footballmanager.session.SessionState
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/profile")
class ProfileController(
    private val sessionState: SessionState,
) {
    @GetMapping("/coach")
    fun coach(): Coach = sessionState.player
}