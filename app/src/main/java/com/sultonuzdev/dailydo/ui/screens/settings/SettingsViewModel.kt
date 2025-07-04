package com.sultonuzdev.dailydo.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sultonuzdev.dailydo.domain.model.AppTheme
import com.sultonuzdev.dailydo.domain.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            userPreferencesRepository.getUserPreferences().collect { preferences ->
                _uiState.update {
                    SettingsUiState(
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
        }
    }

    fun updateTheme(useSystemTheme: Boolean, isDarkMode: Boolean) {
        viewModelScope.launch {
            val theme = when {
                useSystemTheme -> AppTheme.SYSTEM
                isDarkMode -> AppTheme.DARK
                else -> AppTheme.LIGHT
            }
            userPreferencesRepository.updateAppTheme(theme)
        }
    }

    fun updateNotificationSettings(
        notificationsEnabled: Boolean,
        taskReminderEnabled: Boolean,
        dailySummaryEnabled: Boolean,
        achievementNotificationsEnabled: Boolean
    ) {
        viewModelScope.launch {
            userPreferencesRepository.updateNotificationSettings(
                notificationsEnabled,
                taskReminderEnabled,
                dailySummaryEnabled,
                achievementNotificationsEnabled
            )
        }
    }

    fun updateDailySummaryTime(time: String) {
        viewModelScope.launch {
            userPreferencesRepository.updateDailySummaryTime(time)
        }
    }

    fun updateUserProfile(displayName: String, emailAddress: String) {
        viewModelScope.launch {
            userPreferencesRepository.updateUserProfile(displayName, emailAddress)
        }
    }
}

data class SettingsUiState(
    val isDarkMode: Boolean = false,
    val useSystemTheme: Boolean = true,
    val notificationsEnabled: Boolean = true,
    val taskReminderEnabled: Boolean = true,
    val dailySummaryEnabled: Boolean = true,
    val dailySummaryTime: String = "21:00",
    val achievementNotificationsEnabled: Boolean = true,
    val displayName: String = "User",
    val emailAddress: String = ""
)