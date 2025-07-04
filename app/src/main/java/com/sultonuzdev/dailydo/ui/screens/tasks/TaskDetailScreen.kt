package com.sultonuzdev.dailydo.ui.screens.tasks

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sultonuzdev.dailydo.domain.model.Priority

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    modifier: Modifier = Modifier,
    taskId: Long,
    onBackClick: () -> Unit,
    viewModel: TaskDetailViewModel = hiltViewModel()
) {
    // Load task if we have an ID
    LaunchedEffect(taskId) {
        if (taskId != -1L) {
            viewModel.loadTask(taskId)
        }
    }

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (taskId == -1L) "New Task" else "Edit Task",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Go Back"
                        )
                    }
                },
                actions = {
                    if (taskId != -1L) {
                        IconButton(onClick = { viewModel.deleteTask(onBackClick) }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Task"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.saveTask(onBackClick) }) {
                Icon(
                    imageVector = if (taskId == -1L) Icons.Default.Check else Icons.Default.Save,
                    contentDescription = if (taskId == -1L) "Add Task" else "Save Task"
                )
            }
        }
    ) { innerPadding ->
        TaskDetailContent(
            uiState = uiState,
            onTitleChanged = viewModel::updateTitle,
            onDescriptionChanged = viewModel::updateDescription,
            onTargetProgressChanged = viewModel::updateTargetProgress,
            onCurrentProgressChanged = viewModel::updateCurrentProgress,
            onPriorityChanged = viewModel::updatePriority,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun TaskDetailContent(
    uiState: TaskDetailUiState,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onTargetProgressChanged: (Int) -> Unit,
    onCurrentProgressChanged: (Int) -> Unit,
    onPriorityChanged: (Priority) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(48.dp))
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title
                OutlinedTextField(
                    value = uiState.title,
                    onValueChange = onTitleChanged,
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = uiState.titleError != null,
                    supportingText = uiState.titleError?.let { { Text(it) } }
                )

                // Description
                OutlinedTextField(
                    value = uiState.description,
                    onValueChange = onDescriptionChanged,
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5
                )

                // Priority selection
                PrioritySelector(
                    selectedPriority = uiState.priority,
                    onPrioritySelected = onPriorityChanged,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Target progress
                Text(
                    text = "Target Progress: ${uiState.targetProgress}%",
                    style = MaterialTheme.typography.titleMedium
                )

                Slider(
                    value = uiState.targetProgress.toFloat(),
                    onValueChange = { onTargetProgressChanged(it.toInt()) },
                    valueRange = 0f..100f,
                    steps = 100,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Current progress
                Text(
                    text = "Current Progress: ${uiState.currentProgress}%",
                    style = MaterialTheme.typography.titleMedium
                )

                Slider(
                    value = uiState.currentProgress.toFloat(),
                    onValueChange = { onCurrentProgressChanged(it.toInt()) },
                    valueRange = 0f..uiState.targetProgress.toFloat(),
                    steps = uiState.targetProgress,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Complete task button
                if (uiState.currentProgress < uiState.targetProgress) {
                    Button(
                        onClick = { onCurrentProgressChanged(uiState.targetProgress) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Mark as Completed")
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun PrioritySelector(
    selectedPriority: Priority,
    onPrioritySelected: (Priority) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Priority",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PriorityButton(
                text = "Low",
                isSelected = selectedPriority == Priority.LOW,
                onClick = { onPrioritySelected(Priority.LOW) },
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )

            PriorityButton(
                text = "Medium",
                isSelected = selectedPriority == Priority.MEDIUM,
                onClick = { onPrioritySelected(Priority.MEDIUM) },
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.weight(1f)
            )

            PriorityButton(
                text = "High",
                isSelected = selectedPriority == Priority.HIGH,
                onClick = { onPrioritySelected(Priority.HIGH) },
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun PriorityButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    color: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
            containerColor = if (isSelected) color else MaterialTheme.colorScheme.surface,
            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
        )
    ) {
        Text(text = text)
    }
}