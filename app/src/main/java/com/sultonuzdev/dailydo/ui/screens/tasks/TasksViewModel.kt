package com.sultonuzdev.dailydo.ui.screens.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sultonuzdev.dailydo.domain.model.Priority
import com.sultonuzdev.dailydo.domain.model.Task
import com.sultonuzdev.dailydo.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import javax.inject.Inject

/**
 * ViewModel for the Tasks screen
 */
@HiltViewModel
class TasksViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    // Filter state for the tasks
    private val _filterState = MutableStateFlow(TaskFilterState())

    // State for the tasks screen
    val tasksState = combine(
        _filterState,
        taskRepository.getTasksByDate(LocalDate.now())
    ) { filterState, tasks ->
        TasksState(
            isLoading = false,
            tasks = filterTasks(tasks, filterState).map { it.toUiModel() },
            filterState = filterState
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TasksState(isLoading = true)
    )

    /**
     * Update the filter state
     */
    fun updateFilter(filter: TaskFilterState) {
        _filterState.update { filter }
    }

    /**
     * Update a task's progress
     */
    fun updateTaskProgress(taskId: Long, progress: Int) {
        viewModelScope.launch {
            taskRepository.updateTaskProgress(taskId, progress)
        }
    }

    /**
     * Complete a task
     */
    fun completeTask(taskId: Long) {
        viewModelScope.launch {
            taskRepository.completeTask(taskId)
        }
    }

    /**
     * Filter tasks based on the filter state
     */
    private fun filterTasks(tasks: List<Task>, filterState: TaskFilterState): List<Task> {
        var filteredTasks = tasks

        // Filter by completion status
        if (!filterState.showCompleted) {
            filteredTasks = filteredTasks.filter { !it.isCompleted }
        }

        // Filter by priority
        if (filterState.priorityFilter != null) {
            filteredTasks = filteredTasks.filter { it.priority == filterState.priorityFilter }
        }

        // Sort tasks
        filteredTasks = when (filterState.sortOrder) {
            SortOrder.PRIORITY -> filteredTasks.sortedByDescending { it.priority.ordinal }
            SortOrder.PROGRESS -> filteredTasks.sortedBy { it.currentPercentage }
            else -> filteredTasks // Default is by creation date
        }

        return filteredTasks
    }

    /**
     * Extension function to convert a Task to a TaskUiModel
     */
    private fun Task.toUiModel(): TaskUiModel {
        return TaskUiModel(
            id = id,
            title = title,
            description = description,
            progress = currentPercentage,
            targetProgress = targetPercentage,
            isCompleted = isCompleted,
            priority = priority
        )
    }
}

/**
 * State for the Tasks screen
 */
data class TasksState(
    val isLoading: Boolean = false,
    val tasks: List<TaskUiModel> = emptyList(),
    val filterState: TaskFilterState = TaskFilterState()
)

/**
 * Filter state for tasks
 */
data class TaskFilterState(
    val showCompleted: Boolean = true,
    val priorityFilter: Priority? = null,
    val sortOrder: SortOrder = SortOrder.DEFAULT
)

/**
 * Sort order for tasks
 */
enum class SortOrder {
    DEFAULT, PRIORITY, PROGRESS
}

/**
 * UI model for a Task
 */
data class TaskUiModel(
    val id: Long,
    val title: String,
    val description: String,
    val progress: Int,
    val targetProgress: Int,
    val isCompleted: Boolean,
    val priority: Priority
)