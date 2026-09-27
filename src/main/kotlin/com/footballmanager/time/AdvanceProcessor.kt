package com.footballmanager.time

import com.footballmanager.time.enumeration.TimePosition
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class AdvanceProcessor(
    private val timeContext: TimeContext,
) {
    fun advance(): LocalDateTime {
        val time = TimePosition.findByHour(timeContext.getCurrentTime().hour)
        when (time) {
            TimePosition.MORNING -> processMorning()
            TimePosition.PRE_MATCH -> processPreMatch()
            TimePosition.POST_MATCH -> processPostMatch()
            TimePosition.EVENING -> processEvening()
            else -> throw IllegalStateException("Unknown time position")
        }
        return timeContext.getCurrentTime()
    }

    private fun processMorning() {
        val current = timeContext.getCurrentTime()
        val next = current.withHour(TimePosition.PRE_MATCH.hour)
        timeContext.setCurrentTime(next)
    }

    private fun processPreMatch() {
        val current = timeContext.getCurrentTime()
        val next = current.withHour(TimePosition.POST_MATCH.hour)
        timeContext.setCurrentTime(next)
    }

    private fun processPostMatch() {
        val current = timeContext.getCurrentTime()
        val next = current.withHour(TimePosition.EVENING.hour)
        timeContext.setCurrentTime(next)
    }

    private fun processEvening() {
        val current = timeContext.getCurrentTime()
        val next = current.plusDays(1).withHour(TimePosition.MORNING.hour)
        timeContext.setCurrentTime(next)
    }
}