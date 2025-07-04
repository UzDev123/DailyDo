package com.sultonuzdev.dailydo.domain.model

/**
 * Represents user preferences and settings in the Daily Do app.
 */
data class UserPreferences(
    val id: Long = 1, // Only one instance should exist
    val isDarkMode: Boolean = false,
    val useSystemTheme: Boolean = true,
    val notificationsEnabled: Boolean = true,
    val taskReminderEnabled: Boolean = true,
    val dailySummaryEnabled: Boolean = true,
    val dailySummaryTime: String = "21:00", // 24-hour format
    val achievementNotificationsEnabled: Boolean = true,
    val displayName: String = "User",
    val emailAddress: String = ""
)

/**
 * Enum representing different app themes.
 */
enum class AppTheme {
    LIGHT, DARK, SYSTEM;

    companion object {
        fun fromPreferences(preferences: UserPreferences): AppTheme {
            return when {
                preferences.useSystemTheme -> SYSTEM
                preferences.isDarkMode -> DARK
                else -> LIGHT
            }
        }
    }
}