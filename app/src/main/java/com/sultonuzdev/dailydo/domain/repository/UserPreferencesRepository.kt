package com.sultonuzdev.dailydo.domain.repository

import com.sultonuzdev.dailydo.domain.model.AppTheme
import com.sultonuzdev.dailydo.domain.model.UserPreferences
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for UserPreferences-related operations.
 */
interface UserPreferencesRepository {
    /**
     * Get user preferences as a Flow.
     */
    fun getUserPreferences(): Flow<UserPreferences>

    /**
     * Update user preferences.
     */
    suspend fun updateUserPreferences(userPreferences: UserPreferences)

    /**
     * Update app theme.
     */
    suspend fun updateAppTheme(theme: AppTheme)

    /**
     * Update notification settings.
     */
    suspend fun updateNotificationSettings(
        notificationsEnabled: Boolean,
        taskReminderEnabled: Boolean,
        dailySummaryEnabled: Boolean,
        achievementNotificationsEnabled: Boolean
    )

    /**
     * Update daily summary time.
     */
    suspend fun updateDailySummaryTime(time: String)

    /**
     * Update user profile information.
     */
    suspend fun updateUserProfile(displayName: String, emailAddress: String)
}