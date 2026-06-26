package com.footballmanager.domain

import java.time.LocalDate

data class Contract(
    val clubId: Long,
    val wageWeekly: Int,
    val signedDate: LocalDate,
    val expiryDate: LocalDate
) {
    fun isExpired(onDate: LocalDate): Boolean =
        !onDate.isBefore(expiryDate)
}
