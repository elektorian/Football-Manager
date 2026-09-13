package com.footballmanager.application.datetime

import com.footballmanager.domain.datetime.DateTimeProvider
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class DateTimeService(private val dateTimeProvider: DateTimeProvider) {

    fun now(): LocalDateTime = dateTimeProvider.now()

    fun advanceOneSlot(): LocalDateTime = dateTimeProvider.advanceOneSlot()
}
