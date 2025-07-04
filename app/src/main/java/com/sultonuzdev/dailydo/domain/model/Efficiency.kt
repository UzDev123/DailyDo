package com.sultonuzdev.dailydo.domain.model

import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth

/**
 * Represents a daily efficiency record in the Daily Do app.
 */
data class DailyEfficiency(
    val id: Long = 0,
    val date: LocalDate,
    val completedTaskCount: Int = 0,
    val totalTaskCount: Int = 0,
    val completedPercentageSum: Int = 0, // Sum of all tasks' completed percentages
    val targetPercentageSum: Int = 0, // Sum of all tasks' target percentages
    val efficiencyScore: Float = 0f // (completedPercentageSum / targetPercentageSum) * 100
)

/**
 * Represents a weekly efficiency summary.
 */
data class WeeklyEfficiency(
    val weekStartDate: LocalDate,
    val weekEndDate: LocalDate,
    val averageEfficiency: Float,
    val taskCompletionRate: Float, // Completed tasks / Total tasks
    val dailyEfficiencies: List<DailyEfficiency>
)

/**
 * Represents a monthly efficiency summary.
 */
data class MonthlyEfficiency(
    val yearMonth: YearMonth,
    val averageEfficiency: Float,
    val taskCompletionRate: Float,
    val mostProductiveDay: LocalDate?,
    val leastProductiveDay: LocalDate?,
    val dailyEfficiencies: List<DailyEfficiency>
)

/**
 * Represents a yearly efficiency summary.
 */
data class YearlyEfficiency(
    val year: Int,
    val averageEfficiency: Float,
    val taskCompletionRate: Float,
    val mostProductiveMonth: YearMonth?,
    val leastProductiveMonth: YearMonth?,
    val monthlyEfficiencies: List<MonthlyEfficiency>
)