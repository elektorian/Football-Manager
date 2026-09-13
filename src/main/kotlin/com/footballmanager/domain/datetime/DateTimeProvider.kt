package com.footballmanager.domain.datetime

import java.time.LocalDateTime

interface DateTimeProvider {
    fun now(): LocalDateTime
    fun advanceOneSlot(): LocalDateTime
}
