package com.footballmanager.time.controller

import com.footballmanager.time.AdvanceProcessor
import com.footballmanager.time.TimeContext
import com.footballmanager.time.dto.TimeResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TimeController(
    private val advanceProcessor: AdvanceProcessor,
    private val timeContext: TimeContext,
) {
    @PostMapping("/advance")
    fun advance(): TimeResponse {
        return TimeResponse(advanceProcessor.advance())
    }

    @GetMapping("/time")
    fun getTime(): TimeResponse {
        return TimeResponse(timeContext.getCurrentTime())
    }
}