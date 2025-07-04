package com.sultonuzdev.dailydo.data.repository

import com.sultonuzdev.dailydo.data.local.dao.UserPreferencesDao
import com.sultonuzdev.dailydo.data.local.entity.UserPreferencesEntity
import com.sultonuzdev.dailydo.domain.model.AppTheme
import com.sultonuzdev.dailydo.domain.model.UserPreferences
import com.sultonuzdev.dailydo.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesRepositoryImpl @Inject constructor(
    private val userPreferencesDao: UserPreferencesDao
) : UserPreferencesRepository {

    override fun getUserPreferences(): Flow<UserPreferences> {
        return userPreferencesDao.getUserPreferences().map { entity ->
            entity?.toDomain() ?: UserPreferences()
        }
    }

    override suspend fun updateUserPreferences(userPreferences: UserPreferences) {
        val entity = UserPreferencesEntity.fromDomain(userPreferences)
        userPreferencesDao.insertUserPreferences(entity)
    }

    override suspend fun updateAppTheme(theme: AppTheme) {
        val isDarkMode = theme == AppTheme.DARK
        val useSystemTheme = theme == AppTheme.SYSTEM
        userPreferencesDao.updateTheme(isDarkMode, useSystemTheme)
    }

    override suspend fun updateNotificationSettings(
        notificationsEnabled: Boolean,
        taskReminderEnabled: Boolean,
        dailySummaryEnabled: Boolean,
        achievementNotificationsEnabled: Boolean
    ) {
        userPreferencesDao.updateNotificationSettings(
            notificationsEnabled,
            taskReminderEnabled,
            dailySummaryEnabled,
            achievementNotificationsEnabled
        )
    }

    override suspend fun updateDailySummaryTime(time: String) {
        userPreferencesDao.updateDailySummaryTime(time)
    }

    override suspend fun updateUserProfile(displayName: String, emailAddress: String) {
        userPreferencesDao.updateUserProfile(displayName, emailAddress)
    }
}