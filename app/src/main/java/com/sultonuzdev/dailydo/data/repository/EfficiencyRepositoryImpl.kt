package com.sultonuzdev.dailydo.data.repository

import com.sultonuzdev.dailydo.data.local.dao.DailyEfficiencyDao
import com.sultonuzdev.dailydo.data.local.dao.TaskDao
import com.sultonuzdev.dailydo.data.local.entity.DailyEfficiencyEntity
import com.sultonuzdev.dailydo.domain.model.DailyEfficiency
import com.sultonuzdev.dailydo.domain.model.MonthlyEfficiency
import com.sultonuzdev.dailydo.domain.model.WeeklyEfficiency
import com.sultonuzdev.dailydo.domain.model.YearlyEfficiency
import com.sultonuzdev.dailydo.domain.repository.EfficiencyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EfficiencyRepositoryImpl @Inject constructor(
    private val dailyEfficiencyDao: DailyEfficiencyDao,
    private val taskDao: TaskDao
) : EfficiencyRepository {

    override fun getDailyEfficiency(date: LocalDate): Flow<DailyEfficiency?> {
        return dailyEfficiencyDao.getDailyEfficiency(date).map { entity ->
            entity?.toDomain()
        }
    }

    override fun getDailyEfficienciesForRange(
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<List<DailyEfficiency>> {
        return dailyEfficiencyDao.getDailyEfficienciesForRange(startDate, endDate).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getWeeklyEfficiency(weekStartDate: LocalDate): Flow<WeeklyEfficiency?> {
        val weekEndDate = weekStartDate.plusDays(6)

        return getDailyEfficienciesForRange(weekStartDate, weekEndDate).map { dailyEfficiencies ->
            if (dailyEfficiencies.isEmpty()) {
                null
            } else {
                val averageEfficiency =
                    dailyEfficiencies.map { it.efficiencyScore }.average().toFloat()
                val totalCompleted = dailyEfficiencies.sumOf { it.completedTaskCount }
                val totalTasks = dailyEfficiencies.sumOf { it.totalTaskCount }
                val completionRate =
                    if (totalTasks > 0) totalCompleted.toFloat() / totalTasks else 0f

                WeeklyEfficiency(
                    weekStartDate = weekStartDate,
                    weekEndDate = weekEndDate,
                    averageEfficiency = averageEfficiency,
                    taskCompletionRate = completionRate,
                    dailyEfficiencies = dailyEfficiencies
                )
            }
        }
    }

    override fun getMonthlyEfficiency(yearMonth: YearMonth): Flow<MonthlyEfficiency?> {
        val startDate = yearMonth.atDay(1)
        val endDate = yearMonth.atEndOfMonth()

        return getDailyEfficienciesForRange(startDate, endDate).map { dailyEfficiencies ->
            if (dailyEfficiencies.isEmpty()) {
                null
            } else {
                val averageEfficiency =
                    dailyEfficiencies.map { it.efficiencyScore }.average().toFloat()
                val totalCompleted = dailyEfficiencies.sumOf { it.completedTaskCount }
                val totalTasks = dailyEfficiencies.sumOf { it.totalTaskCount }
                val completionRate =
                    if (totalTasks > 0) totalCompleted.toFloat() / totalTasks else 0f

                val mostProductiveDay = dailyEfficiencies.maxByOrNull { it.efficiencyScore }?.date
                val leastProductiveDay = dailyEfficiencies.minByOrNull { it.efficiencyScore }?.date

                MonthlyEfficiency(
                    yearMonth = yearMonth,
                    averageEfficiency = averageEfficiency,
                    taskCompletionRate = completionRate,
                    mostProductiveDay = mostProductiveDay,
                    leastProductiveDay = leastProductiveDay,
                    dailyEfficiencies = dailyEfficiencies
                )
            }
        }
    }

    override fun getYearlyEfficiency(year: Int): Flow<YearlyEfficiency?> {
        val startDate = LocalDate.of(year, 1, 1)
        val endDate = LocalDate.of(year, 12, 31)

        return getDailyEfficienciesForRange(startDate, endDate).map { dailyEfficiencies ->
            if (dailyEfficiencies.isEmpty()) {
                null
            } else {
                // Group by month to create monthly efficiencies
                val efficienciesByMonth = dailyEfficiencies.groupBy { YearMonth.from(it.date) }
                val monthlyEfficiencies = efficienciesByMonth.map { (yearMonth, dailies) ->
                    val avgEfficiency = dailies.map { it.efficiencyScore }.average().toFloat()
                    val totalCompleted = dailies.sumOf { it.completedTaskCount }
                    val totalTasks = dailies.sumOf { it.totalTaskCount }
                    val completionRate =
                        if (totalTasks > 0) totalCompleted.toFloat() / totalTasks else 0f

                    val mostProductiveDay = dailies.maxByOrNull { it.efficiencyScore }?.date
                    val leastProductiveDay = dailies.minByOrNull { it.efficiencyScore }?.date

                    MonthlyEfficiency(
                        yearMonth = yearMonth,
                        averageEfficiency = avgEfficiency,
                        taskCompletionRate = completionRate,
                        mostProductiveDay = mostProductiveDay,
                        leastProductiveDay = leastProductiveDay,
                        dailyEfficiencies = dailies
                    )
                }

                val averageEfficiency =
                    monthlyEfficiencies.map { it.averageEfficiency }.average().toFloat()
                val totalCompleted = dailyEfficiencies.sumOf { it.completedTaskCount }
                val totalTasks = dailyEfficiencies.sumOf { it.totalTaskCount }
                val completionRate =
                    if (totalTasks > 0) totalCompleted.toFloat() / totalTasks else 0f

                val mostProductiveMonth =
                    monthlyEfficiencies.maxByOrNull { it.averageEfficiency }?.yearMonth
                val leastProductiveMonth =
                    monthlyEfficiencies.minByOrNull { it.averageEfficiency }?.yearMonth

                YearlyEfficiency(
                    year = year,
                    averageEfficiency = averageEfficiency,
                    taskCompletionRate = completionRate,
                    mostProductiveMonth = mostProductiveMonth,
                    leastProductiveMonth = leastProductiveMonth,
                    monthlyEfficiencies = monthlyEfficiencies
                )
            }
        }
    }

    override suspend fun updateDailyEfficiency(efficiency: DailyEfficiency) {
        val entity = DailyEfficiencyEntity.fromDomain(efficiency)
        dailyEfficiencyDao.insertDailyEfficiency(entity)
    }

    override suspend fun calculateAndUpdateEfficiencyForDate(date: LocalDate) {
        // Calculate efficiency for the date based on tasks
        val totalTaskCount = taskDao.getTaskCountForDate(date)
        val completedTaskCount = taskDao.getCompletedTaskCountForDate(date)
        val targetPercentageSum = taskDao.getTargetPercentageSumForDate(date) ?: 0
        val completedPercentageSum = taskDao.getCompletedPercentageSumForDate(date) ?: 0

        // Calculate efficiency score
        val efficiencyScore = if (targetPercentageSum > 0) {
            (completedPercentageSum.toFloat() / targetPercentageSum) * 100
        } else {
            0f
        }

        // Create and insert the efficiency entity
        val dailyEfficiency = DailyEfficiency(
            date = date,
            completedTaskCount = completedTaskCount,
            totalTaskCount = totalTaskCount,
            completedPercentageSum = completedPercentageSum,
            targetPercentageSum = targetPercentageSum,
            efficiencyScore = efficiencyScore
        )

        updateDailyEfficiency(dailyEfficiency)
    }

    override fun getAverageEfficiencyForLastDays(days: Int): Flow<Float> {
        val startDate = LocalDate.now().minusDays(days.toLong() - 1)
        return dailyEfficiencyDao.getAverageEfficiencySince(startDate).map { it ?: 0f }
    }
}