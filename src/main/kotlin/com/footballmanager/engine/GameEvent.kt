package com.footballmanager.engine

import java.time.LocalDate

sealed interface GameEvent {
    val date: LocalDate
}
