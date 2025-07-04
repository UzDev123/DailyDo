package com.sultonuzdev.dailydo.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    // In a real implementation, we would use the ViewModel to load and save settings
    // For this example, we'll just use local state
    val uiState by viewModel.uiState.collectAsState()

    var isDarkMode by remember { mutableStateOf(uiState.isDarkMode) }
    var useSystemTheme by remember { mutableStateOf(uiState.useSystemTheme) }
    var notificationsEnabled by remember { mutableStateOf(uiState.notificationsEnabled) }
    var taskReminderEnabled by remember { mutableStateOf(uiState.taskReminderEnabled) }
    var dailySummaryEnabled by remember { mutableStateOf(uiState.dailySummaryEnabled) }
    var achievementNotificationsEnabled by remember { mutableStateOf(uiState.achievementNotificationsEnabled) }

    Column(modifier = modifier.fillMaxSize()) {
        // TopAppBar
        TopAppBar(
            title = {
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                titleContentColor = MaterialTheme.colorScheme.onBackground
            )
        )

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        ) {
            // Theme settings
            SettingsSection(title = "Theme") {
                SettingsSwitch(
                    title = "Use System Theme",
                    description = "Automatically switch between light and dark theme based on system settings",
                    icon = Icons.Filled.Settings,
                    checked = useSystemTheme,
                    onCheckedChange = {
                        useSystemTheme = it
                        if (!it) {
                            // If not using system theme, default to light mode
                            isDarkMode = false
                        }
                        viewModel.updateTheme(useSystemTheme = it, isDarkMode = isDarkMode)
                    }
                )

                if (!useSystemTheme) {
                    SettingsSwitch(
                        title = "Dark Mode",
                        description = "Enable dark theme",
                        icon = Icons.Filled.Settings,
                        checked = isDarkMode,
                        onCheckedChange = {
                            isDarkMode = it
                            viewModel.updateTheme(useSystemTheme = useSystemTheme, isDarkMode = it)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Notification settings
            SettingsSection(title = "Notifications") {
                SettingsSwitch(
                    title = "Enable Notifications",
                    description = "Allow the app to send notifications",
                    icon = if (notificationsEnabled)
                        Icons.Filled.Notifications
                    else
                        Icons.Filled.Info,
                    checked = notificationsEnabled,
                    onCheckedChange = {
                        notificationsEnabled = it
                        if (!it) {
                            // Disable all notification types if main switch is off
                            taskReminderEnabled = false
                            dailySummaryEnabled = false
                            achievementNotificationsEnabled = false
                        }
                        viewModel.updateNotificationSettings(
                            notificationsEnabled = it,
                            taskReminderEnabled = taskReminderEnabled,
                            dailySummaryEnabled = dailySummaryEnabled,
                            achievementNotificationsEnabled = achievementNotificationsEnabled
                        )
                    }
                )

                if (notificationsEnabled) {
                    SettingsSwitch(
                        title = "Task Reminders",
                        description = "Receive reminders for upcoming tasks",
                        icon = Icons.Filled.Notifications,
                        checked = taskReminderEnabled,
                        onCheckedChange = {
                            taskReminderEnabled = it
                            viewModel.updateNotificationSettings(
                                notificationsEnabled = notificationsEnabled,
                                taskReminderEnabled = it,
                                dailySummaryEnabled = dailySummaryEnabled,
                                achievementNotificationsEnabled = achievementNotificationsEnabled
                            )
                        }
                    )

                    SettingsSwitch(
                        title = "Daily Summary",
                        description = "Receive a daily summary of your productivity",
                        icon = Icons.Filled.Notifications,
                        checked = dailySummaryEnabled,
                        onCheckedChange = {
                            dailySummaryEnabled = it
                            viewModel.updateNotificationSettings(
                                notificationsEnabled = notificationsEnabled,
                                taskReminderEnabled = taskReminderEnabled,
                                dailySummaryEnabled = it,
                                achievementNotificationsEnabled = achievementNotificationsEnabled
                            )
                        }
                    )

                    SettingsSwitch(
                        title = "Achievement Notifications",
                        description = "Receive notifications for achievements",
                        icon = Icons.Filled.Notifications,
                        checked = achievementNotificationsEnabled,
                        onCheckedChange = {
                            achievementNotificationsEnabled = it
                            viewModel.updateNotificationSettings(
                                notificationsEnabled = notificationsEnabled,
                                taskReminderEnabled = taskReminderEnabled,
                                dailySummaryEnabled = dailySummaryEnabled,
                                achievementNotificationsEnabled = it
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // User profile
            SettingsSection(title = "User Profile") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "User",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "Default user account",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // App info
            Text(
                text = "Daily Do v1.0",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            content()
        }
    }
}

@Composable
private fun SettingsSwitch(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}