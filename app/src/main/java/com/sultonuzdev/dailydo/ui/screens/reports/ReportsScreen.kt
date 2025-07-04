package com.sultonuzdev.dailydo.ui.screens.reports

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(
    modifier: Modifier = Modifier,
    viewModel: ReportsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTimeframeIndex by remember { mutableStateOf(0) }
    val timeframes = listOf("Daily", "Weekly", "Monthly", "Yearly")

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Efficiency Reports",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Time period selector
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth()
            ) {
                timeframes.forEachIndexed { index, label ->
                    SegmentedButton(
                        selected = selectedTimeframeIndex == index,
                        onClick = {
                            selectedTimeframeIndex = index
                            viewModel.changeTimeframe(getTimeframeFromIndex(index))
                        },
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = timeframes.size
                        ),
                        label = { Text(label) }
                    )
                }
            }

            // Reports content based on selected timeframe
            ReportsContent(
                uiState = uiState,
                selectedTimeframe = getTimeframeFromIndex(selectedTimeframeIndex),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ReportsContent(
    uiState: ReportsUiState,
    selectedTimeframe: Timeframe,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else if (uiState.hasNoData) {
            EmptyReportsPlaceholder()
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Efficiency display
                EfficiencyDisplay(
                    efficiency = when (selectedTimeframe) {
                        Timeframe.DAILY -> uiState.dailyEfficiency
                        Timeframe.WEEKLY -> uiState.weeklyEfficiency
                        Timeframe.MONTHLY -> uiState.monthlyEfficiency
                        Timeframe.YEARLY -> uiState.yearlyEfficiency
                    },
                    timeframe = selectedTimeframe,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Statistics cards
                StatisticsCards(
                    uiState = uiState,
                    selectedTimeframe = selectedTimeframe,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Charts section
                // Note: In a real app, we'd integrate MPAndroidChart here
                Text(
                    text = "Performance Trend",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                ChartPlaceholder(modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp))

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun EfficiencyDisplay(
    efficiency: Float,
    timeframe: Timeframe,
    modifier: Modifier = Modifier
) {
    val circleColor = when {
        efficiency >= 90 -> Color(0xFF4CAF50) // Green
        efficiency >= 70 -> Color(0xFF8BC34A) // Light Green
        efficiency >= 50 -> Color(0xFFFFC107) // Amber
        efficiency >= 30 -> Color(0xFFFF9800) // Orange
        else -> Color(0xFFF44336) // Red
    }

    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${timeframe.displayName} Efficiency",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Circular progress indicator
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = efficiency / 100f,
                    modifier = Modifier.size(160.dp),
                    strokeWidth = 12.dp,
                    color = circleColor
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${efficiency.toInt()}%",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = circleColor
                    )
                    Text(
                        text = "Efficiency",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = getEfficiencyMessage(efficiency),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun StatisticsCards(
    uiState: ReportsUiState,
    selectedTimeframe: Timeframe,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Key Statistics",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Task completion rate
        Card(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Task Completion Rate",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = when (selectedTimeframe) {
                            Timeframe.DAILY -> "${uiState.dailyTaskCompletionRate.toInt()}%"
                            Timeframe.WEEKLY -> "${uiState.weeklyTaskCompletionRate.toInt()}%"
                            Timeframe.MONTHLY -> "${uiState.monthlyTaskCompletionRate.toInt()}%"
                            Timeframe.YEARLY -> "${uiState.yearlyTaskCompletionRate.toInt()}%"
                        },
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Simple circular indicator
                val completionRate = when (selectedTimeframe) {
                    Timeframe.DAILY -> uiState.dailyTaskCompletionRate
                    Timeframe.WEEKLY -> uiState.weeklyTaskCompletionRate
                    Timeframe.MONTHLY -> uiState.monthlyTaskCompletionRate
                    Timeframe.YEARLY -> uiState.yearlyTaskCompletionRate
                }

                CircularProgressIndicator(
                    progress = completionRate / 100f,
                    modifier = Modifier.size(48.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun ChartPlaceholder(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
                MaterialTheme.shapes.medium
            )
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Performance Chart\n(MPAndroidChart would be integrated here)",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun EmptyReportsPlaceholder() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "No data available",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Complete some tasks to see your efficiency reports",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

private fun getTimeframeFromIndex(index: Int): Timeframe {
    return when (index) {
        0 -> Timeframe.DAILY
        1 -> Timeframe.WEEKLY
        2 -> Timeframe.MONTHLY
        3 -> Timeframe.YEARLY
        else -> Timeframe.DAILY
    }
}

private fun getEfficiencyMessage(efficiency: Float): String {
    return when {
        efficiency >= 90 -> "Excellent! You're performing at your best."
        efficiency >= 70 -> "Good job! You're making great progress."
        efficiency >= 50 -> "You're doing well. Keep pushing forward!"
        efficiency >= 30 -> "You're on your way. Keep improving!"
        else -> "There's room for improvement. Stay motivated!"
    }
}