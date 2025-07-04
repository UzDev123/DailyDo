package com.sultonuzdev.dailydo.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sultonuzdev.dailydo.data.local.entity.DailyEfficiencyEntity
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.LocalDate

@Dao
interface DailyEfficiencyDao {
    @Query("SELECT * FROM daily_efficiency WHERE date = :date")
    fun getDailyEfficiency(date: LocalDate): Flow<DailyEfficiencyEntity?>

    @Query("SELECT * FROM daily_efficiency WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    fun getDailyEfficienciesForRange(
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<List<DailyEfficiencyEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyEfficiency(efficiency: DailyEfficiencyEntity)

    @Update
    suspend fun updateDailyEfficiency(efficiency: DailyEfficiencyEntity)

    @Query("SELECT AVG(efficiencyScore) FROM daily_efficiency WHERE date >= :startDate")
    fun getAverageEfficiencySince(startDate: LocalDate): Flow<Float?>

    @Query("SELECT * FROM daily_efficiency WHERE date >= :startDate ORDER BY efficiencyScore DESC LIMIT 1")
    fun getMostProductiveDaySince(startDate: LocalDate): Flow<DailyEfficiencyEntity?>

    @Query("SELECT * FROM daily_efficiency WHERE date >= :startDate ORDER BY efficiencyScore ASC LIMIT 1")
    fun getLeastProductiveDaySince(startDate: LocalDate): Flow<DailyEfficiencyEntity?>

    @Query("SELECT * FROM daily_efficiency WHERE date >= :startDate ORDER BY date DESC")
    fun getDailyEfficienciesSince(startDate: LocalDate): Flow<List<DailyEfficiencyEntity>>
}