package com.sultonuzdev.dailydo.ui.screens.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sultonuzdev.dailydo.domain.model.Priority
import com.sultonuzdev.dailydo.domain.model.Task
import com.sultonuzdev.dailydo.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import javax.inject.Inject

/**
 * ViewModel for the Task Detail screen
 */
@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskDetailUiState())
    val uiState: StateFlow<TaskDetailUiState> = _uiState.asStateFlow()

    /**
     * Load a task by ID
     */
    fun loadTask(taskId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            taskRepository.getTaskById(taskId).collect { task ->
                if (task != null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            title = task.title,
                            description = task.description,
                            targetProgress = task.targetPercentage,
                            currentProgress = task.currentPercentage,
                            priority = task.priority,
                            isCompleted = task.isCompleted
                        )
                    }
                }
            }
        }
    }

    /**
     * Save the current task
     */
    fun saveTask(onSuccess: () -> Unit) {
        viewModelScope.launch {
            if (!validateInputs()) {
                return@launch
            }

            val task = Task(
                id = if (_uiState.value.isNewTask) 0 else _uiState.value.taskId,
                title = _uiState.value.title,
                description = _uiState.value.description,
                targetPercentage = _uiState.value.targetProgress,
                currentPercentage = _uiState.value.currentProgress,
                createdDate = LocalDate.now(),
                deadline = null,
                isCompleted = _uiState.value.currentProgress >= _uiState.value.targetProgress,
                priority = _uiState.value.priority
            )

            taskRepository.upsertTask(task)
            onSuccess()
        }
    }

    /**
     * Delete the current task
     */
    fun deleteTask(onSuccess: () -> Unit) {
        viewModelScope.launch {
            if (_uiState.value.taskId > 0) {
                val task = Task(
                    id = _uiState.value.taskId,
                    title = _uiState.value.title,
                    description = _uiState.value.description,
                    targetPercentage = _uiState.value.targetProgress,
                    currentPercentage = _uiState.value.currentProgress,
                    priority = _uiState.value.priority
                )
                taskRepository.deleteTask(task)
                onSuccess()
            }
        }
    }

    /**
     * Update the task title
     */
    fun updateTitle(title: String) {
        _uiState.update {
            it.copy(
                title = title,
                titleError = if (title.isBlank()) "Title cannot be empty" else null
            )
        }
    }

    /**
     * Update the task description
     */
    fun updateDescription(description: String) {
        _uiState.update { it.copy(description = description) }
    }

    /**
     * Update the target progress
     */
    fun updateTargetProgress(progress: Int) {
        _uiState.update {
            val newTargetProgress = progress.coerceIn(1, 100)
            val newCurrentProgress = it.currentProgress.coerceAtMost(newTargetProgress)
            it.copy(
                targetProgress = newTargetProgress,
                currentProgress = newCurrentProgress
            )
        }
    }

    /**
     * Update the current progress
     */
    fun updateCurrentProgress(progress: Int) {
        _uiState.update {
            val newProgress = progress.coerceIn(0, it.targetProgress)
            it.copy(currentProgress = newProgress)
        }
    }

    /**
     * Update the task priority
     */
    fun updatePriority(priority: Priority) {
        _uiState.update { it.copy(priority = priority) }
    }

    /**
     * Validate the inputs
     */
    private fun validateInputs(): Boolean {
        var isValid = true

        if (_uiState.value.title.isBlank()) {
            _uiState.update { it.copy(titleError = "Title cannot be empty") }
            isValid = false
        }

        if (_uiState.value.targetProgress <= 0) {
            isValid = false
        }

        return isValid
    }
}

/**
 * UI state for the Task Detail screen
 */
data class TaskDetailUiState(
    val isLoading: Boolean = false,
    val isNewTask: Boolean = true,
    val taskId: Long = 0,
    val title: String = "",
    val titleError: String? = null,
    val description: String = "",
    val targetProgress: Int = 100,
    val currentProgress: Int = 0,
    val priority: Priority = Priority.MEDIUM,
    val isCompleted: Boolean = false
)