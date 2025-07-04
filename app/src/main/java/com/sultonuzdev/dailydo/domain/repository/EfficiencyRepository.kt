package com.sultonuzdev.dailydo.domain.repository

import com.sultonuzdev.dailydo.domain.model.DailyEfficiency
import com.sultonuzdev.dailydo.domain.model.MonthlyEfficiency
import com.sultonuzdev.dailydo.domain.model.WeeklyEfficiency
import com.sultonuzdev.dailydo.domain.model.YearlyEfficiency
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth

/**
 * Repository interface for Efficiency-related operations.
 */
interface EfficiencyRepository {
    /**
     * Get daily efficiency for a specific date as a Flow.
     */
    fun getDailyEfficiency(date: LocalDate): Flow<DailyEfficiency?>

    /**
     * Get daily efficiencies for a date range as a Flow.
     */
    fun getDailyEfficienciesForRange(
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<List<DailyEfficiency>>

    /**
     * Get weekly efficiency for a specific week as a Flow.
     */
    fun getWeeklyEfficiency(weekStartDate: LocalDate): Flow<WeeklyEfficiency?>

    /**
     * Get monthly efficiency for a specific month as a Flow.
     */
    fun getMonthlyEfficiency(yearMonth: YearMonth): Flow<MonthlyEfficiency?>

    /**
     * Get yearly efficiency for a specific year as a Flow.
     */
    fun getYearlyEfficiency(year: Int): Flow<YearlyEfficiency?>

    /**
     * Update or insert daily efficiency.
     */
    suspend fun updateDailyEfficiency(efficiency: DailyEfficiency)

    /**
     * Calculate and update efficiency for a specific date.
     */
    suspend fun calculateAndUpdateEfficiencyForDate(date: LocalDate)

    /**
     * Get average efficiency for the last N days.
     */
    fun getAverageEfficiencyForLastDays(days: Int): Flow<Float>
}