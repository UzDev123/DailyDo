package com.sultonuzdev.dailydo.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sultonuzdev.dailydo.data.local.entity.UserPreferencesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserPreferencesDao {
    @Query("SELECT * FROM user_preferences WHERE id = 1")
    fun getUserPreferences(): Flow<UserPreferencesEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserPreferences(preferences: UserPreferencesEntity)

    @Update
    suspend fun updateUserPreferences(preferences: UserPreferencesEntity)

    @Query("UPDATE user_preferences SET isDarkMode = :isDarkMode, useSystemTheme = :useSystemTheme WHERE id = 1")
    suspend fun updateTheme(isDarkMode: Boolean, useSystemTheme: Boolean)

    @Query("UPDATE user_preferences SET notificationsEnabled = :notificationsEnabled, taskReminderEnabled = :taskReminderEnabled, dailySummaryEnabled = :dailySummaryEnabled, achievementNotificationsEnabled = :achievementNotificationsEnabled WHERE id = 1")
    suspend fun updateNotificationSettings(
        notificationsEnabled: Boolean,
        taskReminderEnabled: Boolean,
        dailySummaryEnabled: Boolean,
        achievementNotificationsEnabled: Boolean
    )

    @Query("UPDATE user_preferences SET dailySummaryTime = :time WHERE id = 1")
    suspend fun updateDailySummaryTime(time: String)

    @Query("UPDATE user_preferences SET displayName = :displayName, emailAddress = :emailAddress WHERE id = 1")
    suspend fun updateUserProfile(displayName: String, emailAddress: String)
}