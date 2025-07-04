package com.sultonuzdev.dailydo.domain.repository

import com.sultonuzdev.dailydo.domain.model.Task
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.LocalDate

/**
 * Repository interface for Task-related operations.
 */
interface TaskRepository {
    /**
     * Get all tasks as a Flow.
     */
    fun getAllTasks(): Flow<List<Task>>

    /**
     * Get tasks for a specific date as a Flow.
     */
    fun getTasksByDate(date: LocalDate): Flow<List<Task>>

    /**
     * Get a task by its ID as a Flow.
     */
    fun getTaskById(id: Long): Flow<Task?>

    /**
     * Insert or update a task.
     * @return The ID of the inserted/updated task
     */
    suspend fun upsertTask(task: Task): Long

    /**
     * Delete a task.
     */
    suspend fun deleteTask(task: Task)

    /**
     * Update task progress.
     */
    suspend fun updateTaskProgress(taskId: Long, currentPercentage: Int)

    /**
     * Complete a task.
     */
    suspend fun completeTask(taskId: Long)

    /**
     * Get incomplete tasks as a Flow.
     */
    fun getIncompleteTasks(): Flow<List<Task>>
}