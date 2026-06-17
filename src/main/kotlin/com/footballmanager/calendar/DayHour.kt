package com.footballmanager.calendar

enum class DayHour(val value: Int) {
    START_HOUR(8),
    MATCH_HOUR(16),
    END_HOUR(22),
    ;

    companion object {
        fun getByValue(value: Int): DayHour = entries.find { it.value == value }!!
    }
}