package com.footballmanager.domain.core.person

import java.math.BigDecimal
import java.math.RoundingMode

object SalaryCeiler {
    private val ROUND_25 = BigDecimal.valueOf(25)
    private val ROUND_100 = BigDecimal.valueOf(100)
    private val ROUND_500 = BigDecimal.valueOf(500)
    private val ROUND_1K = BigDecimal.valueOf(1_000)
    private val ROUND_5K = BigDecimal.valueOf(5_000)

    private val THRESHOLD_1K = BigDecimal.valueOf(1_000)
    private val THRESHOLD_10K = BigDecimal.valueOf(10_000)
    private val THRESHOLD_100K = BigDecimal.valueOf(100_000)
    private val THRESHOLD_150K = BigDecimal.valueOf(150_000)

    fun ceiling(salary: BigDecimal): BigDecimal {
        require(salary >= BigDecimal.ZERO) { "salary must be non-negative" }
        if (salary == BigDecimal.ZERO) return salary

        val multiple = when {
            salary < THRESHOLD_1K -> ROUND_25
            salary < THRESHOLD_10K -> ROUND_100
            salary < THRESHOLD_100K -> ROUND_500
            salary < THRESHOLD_150K -> ROUND_1K
            else -> ROUND_5K
        }

        return (salary / multiple).setScale(0, RoundingMode.HALF_UP) * multiple
    }
}