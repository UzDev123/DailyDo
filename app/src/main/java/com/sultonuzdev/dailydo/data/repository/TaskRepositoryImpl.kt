package com.sultonuzdev.dailydo.data.repository

import com.sultonuzdev.dailydo.data.local.dao.TaskDao
import com.sultonuzdev.dailydo.data.local.entity.TaskEntity
import com.sultonuzdev.dailydo.domain.model.Task
import com.sultonuzdev.dailydo.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.threeten.bp.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao
) : TaskRepository {

    override fun getAllTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getTasksByDate(date: LocalDate): Flow<List<Task>> {
        return taskDao.getTasksByDate(date).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getTaskById(id: Long): Flow<Task?> {
        return taskDao.getTaskById(id).map { entity ->
            entity?.toDomain()
        }
    }

    override suspend fun upsertTask(task: Task): Long {
        val entity = TaskEntity.fromDomain(task)
        return taskDao.insertTask(entity)
    }

    override suspend fun deleteTask(task: Task) {
        val entity = TaskEntity.fromDomain(task)
        taskDao.deleteTask(entity)
    }

    override suspend fun updateTaskProgress(taskId: Long, currentPercentage: Int) {
        taskDao.updateTaskProgress(taskId, currentPercentage)
    }

    override suspend fun completeTask(taskId: Long) {
        taskDao.completeTask(taskId)
    }

    override fun getIncompleteTasks(): Flow<List<Task>> {
        return taskDao.getIncompleteTasks().map { entities ->
            entities.map { it.toDomain() }
        }
    }
}