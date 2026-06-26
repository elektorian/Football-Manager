package com.footballmanager.controller

import com.footballmanager.engine.GameEngine
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/engine")
class EngineController(
    private val engine: GameEngine
) {

    @GetMapping("/state")
    fun getState(): Map<String, Any> {
        return mapOf(
            "date" to engine.world.clock.currentDate.toString()
        )
    }

    @PostMapping("/advance")
    fun advance(@RequestParam(defaultValue = "1") days: Int): Map<String, Any> {
        val events = engine.advance(days)
        return mapOf(
            "date" to engine.world.clock.currentDate.toString(),
            "events" to events.map { it::class.simpleName }
        )
    }
}
