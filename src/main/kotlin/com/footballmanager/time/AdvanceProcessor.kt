package com.footballmanager.time

import com.footballmanager.messages.MessageProducer
import com.footballmanager.messages.MessageService
import com.footballmanager.model.match.MatchResultCalculator
import com.footballmanager.model.match.repository.MatchRepository
import com.footballmanager.time.enumeration.TimePosition
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class AdvanceProcessor(
    private val timeContext: TimeContext,
    private val matchRepository: MatchRepository,
    private val matchResultCalculator: MatchResultCalculator,
    private val messageProducer: MessageProducer,
    private val messageService: MessageService,
) {
    fun advance(): LocalDateTime {
        if (messageService.hasUnread()) return timeContext.getCurrentTime()
        
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

        val todayMatches = matchRepository.getTodayMatches()
        messageProducer.registerMessageOverviewMessage(todayMatches)
    }

    private fun processPostMatch() {
        val current = timeContext.getCurrentTime()
        val next = current.withHour(TimePosition.EVENING.hour)
        timeContext.setCurrentTime(next)

        val todayMatches = matchRepository.getTodayMatches()
        todayMatches.forEach { matchResultCalculator.calculate(it) }
        messageProducer.registerMessageOverviewMessage(todayMatches)
    }

    private fun processEvening() {
        val current = timeContext.getCurrentTime()
        val next = current.plusDays(1).withHour(TimePosition.MORNING.hour)
        timeContext.setCurrentTime(next)
    }
}