package com.sultonuzdev.dailydo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sultonuzdev.dailydo.domain.model.UserPreferences

@Entity(tableName = "user_preferences")
data class UserPreferencesEntity(
    @PrimaryKey
    val id: Long = 1, // Only one instance should exist
    val isDarkMode: Boolean,
    val useSystemTheme: Boolean,
    val notificationsEnabled: Boolean,
    val taskReminderEnabled: Boolean,
    val dailySummaryEnabled: Boolean,
    val dailySummaryTime: String,
    val achievementNotificationsEnabled: Boolean,
    val displayName: String,
    val emailAddress: String
) {
    companion object {
        fun fromDomain(preferences: UserPreferences): UserPreferencesEntity {
            return UserPreferencesEntity(
                id = preferences.id,
                isDarkMode = preferences.isDarkMode,
                useSystemTheme = preferences.useSystemTheme,
                notificationsEnabled = preferences.notificationsEnabled,
                taskReminderEnabled = preferences.taskReminderEnabled,
                dailySummaryEnabled = preferences.dailySummaryEnabled,
                dailySummaryTime = preferences.dailySummaryTime,
                achievementNotificationsEnabled = preferences.achievementNotificationsEnabled,
                displayName = preferences.displayName,
                emailAddress = preferences.emailAddress
            )
        }
    }

    fun toDomain(): UserPreferences {
        return UserPreferences(
            id = id,
            isDarkMode = isDarkMode,
            useSystemTheme = useSystemTheme,
            notificationsEnabled = notificationsEnabled,
            taskReminderEnabled = taskReminderEnabled,
            dailySummaryEnabled = dailySummaryEnabled,
            dailySummaryTime = dailySummaryTime,
            achievementNotificationsEnabled = achievementNotificationsEnabled,
            displayName = displayName,
            emailAddress = emailAddress
        )
    }
}