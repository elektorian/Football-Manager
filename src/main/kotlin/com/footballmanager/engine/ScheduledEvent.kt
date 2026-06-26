package com.footballmanager.engine

import java.time.LocalDate

sealed interface ScheduledEvent {
    val date: LocalDate
}
