package com.sultonuzdev.dailydo.ui.screens.tasks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sultonuzdev.dailydo.domain.model.Priority
import com.sultonuzdev.dailydo.ui.theme.DailyDoTheme
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(

    onTaskClick: (Long) -> Unit,
    onAddTaskClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TasksViewModel = hiltViewModel()

) {
    val tasksState by viewModel.tasksState.collectAsState()
    val currentDate = LocalDate.now()
    val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = currentDate.format(dateFormatter),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddTaskClick,
                containerColor = MaterialTheme.colorScheme.primary,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Task"
                )
            }
        }
    ) { innerPadding ->
        TasksContent(
            tasksState = tasksState,
            onTaskClick = onTaskClick,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun TasksContent(
    tasksState: TasksState,
    onTaskClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when {
            tasksState.isLoading -> {
                CircularProgressIndicator()
            }

            tasksState.tasks.isEmpty() -> {
                EmptyTasksPlaceholder()
            }

            else -> {
                TasksList(
                    tasks = tasksState.tasks,
                    onTaskClick = onTaskClick,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun TasksList(
    tasks: List<TaskUiModel>,
    onTaskClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(tasks) { task ->
            TaskItem(
                task = task,
                onClick = { onTaskClick(task.id) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun EmptyTasksPlaceholder() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "No tasks for today",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Add a task to get started",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
@Preview(showBackground = true)
@Composable
fun TasksScreenPreview() {
    val sampleTask = TaskUiModel(
        id = 1L,
        title = "Sample Task",
        description = "This is a sample task for DailyDo",
        progress = 50,
        targetProgress = 100,
        isCompleted = false,
        priority = Priority.MEDIUM
    )

    DailyDoTheme {
        TasksContent(
            onTaskClick = {},
            tasksState = TasksState(
                tasks = listOf(sampleTask),
                isLoading = false
            ),
            modifier = Modifier
        )
    }
}