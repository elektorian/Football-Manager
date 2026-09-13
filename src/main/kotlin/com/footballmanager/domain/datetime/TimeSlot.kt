package com.footballmanager.domain.datetime

import java.time.LocalTime

enum class TimeSlot(val time: LocalTime) {
    Start(LocalTime.of(8, 0)),
    PreMatch(LocalTime.of(15, 0)),
    PostMatch(LocalTime.of(18, 0)),
    EndDay(LocalTime.of(23, 0));

    fun next(): TimeSlot {
        return entries[(ordinal + 1) % entries.size]
    }
}
