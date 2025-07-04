package com.sultonuzdev.dailydo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sultonuzdev.dailydo.domain.model.DailyEfficiency
import org.threeten.bp.LocalDate

@Entity(tableName = "daily_efficiency")
data class DailyEfficiencyEntity(
    @PrimaryKey
    val date: LocalDate,
    val completedTaskCount: Int,
    val totalTaskCount: Int,
    val completedPercentageSum: Int,
    val targetPercentageSum: Int,
    val efficiencyScore: Float
) {
    companion object {
        fun fromDomain(efficiency: DailyEfficiency): DailyEfficiencyEntity {
            return DailyEfficiencyEntity(
                date = efficiency.date,
                completedTaskCount = efficiency.completedTaskCount,
                totalTaskCount = efficiency.totalTaskCount,
                completedPercentageSum = efficiency.completedPercentageSum,
                targetPercentageSum = efficiency.targetPercentageSum,
                efficiencyScore = efficiency.efficiencyScore
            )
        }
    }

    fun toDomain(): DailyEfficiency {
        return DailyEfficiency(
            date = date,
            completedTaskCount = completedTaskCount,
            totalTaskCount = totalTaskCount,
            completedPercentageSum = completedPercentageSum,
            targetPercentageSum = targetPercentageSum,
            efficiencyScore = efficiencyScore
        )
    }
}