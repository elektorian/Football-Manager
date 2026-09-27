package com.footballmanager.time.enumeration

enum class TimePosition(
    val hour: Int,
) {
    MORNING(8),
    PRE_MATCH(16),
    POST_MATCH(18),
    EVENING(23),
    ;
    companion object {
        fun findByHour(hour: Int): TimePosition? {
            return entries.find { it.hour == hour }
        }
    }
}
