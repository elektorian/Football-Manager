package com.footballmanager.calendar

import com.footballmanager.application.events.NewDayEvent
import com.footballmanager.configuration.GlobalParameters
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Component
class CurrentMomentHolder(
    private val eventPublisher: ApplicationEventPublisher,
) {

    private var currentMoment: LocalDateTime = LocalDateTime.of(
        LocalDate.of(GlobalParameters.START_YEAR, 7, 1),
        LocalTime.of(8, 0)
    )

    fun setHour(hour: DayHour) {
        set(currentMoment.withHour(hour.value))
    }

    @Synchronized
    fun set(newCurrentMoment: LocalDateTime) {
        if (newCurrentMoment == currentMoment) return
        if (newCurrentMoment < currentMoment) throw IllegalStateException("Время не может двигаться взад")
        currentMoment = newCurrentMoment
        if (currentMoment.hour == DayHour.START_HOUR.value) {
            eventPublisher.publishEvent(NewDayEvent)
        }
    }

    @Synchronized
    fun get(): LocalDateTime = currentMoment
}