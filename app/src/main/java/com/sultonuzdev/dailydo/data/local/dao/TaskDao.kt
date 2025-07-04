package com.sultonuzdev.dailydo.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sultonuzdev.dailydo.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.LocalDate

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY createdDate DESC")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE createdDate = :date ORDER BY priority DESC")
    fun getTasksByDate(date: LocalDate): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    fun getTaskById(id: Long): Flow<TaskEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity): Long

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Query("UPDATE tasks SET currentPercentage = :percentage WHERE id = :taskId")
    suspend fun updateTaskProgress(taskId: Long, percentage: Int)

    @Query("UPDATE tasks SET isCompleted = 1, currentPercentage = targetPercentage WHERE id = :taskId")
    suspend fun completeTask(taskId: Long)

    @Query("SELECT * FROM tasks WHERE isCompleted = 0 ORDER BY deadline")
    fun getIncompleteTasks(): Flow<List<TaskEntity>>

    @Query("SELECT COUNT(*) FROM tasks WHERE createdDate = :date")
    suspend fun getTaskCountForDate(date: LocalDate): Int

    @Query("SELECT COUNT(*) FROM tasks WHERE createdDate = :date AND isCompleted = 1")
    suspend fun getCompletedTaskCountForDate(date: LocalDate): Int

    @Query("SELECT SUM(targetPercentage) FROM tasks WHERE createdDate = :date")
    suspend fun getTargetPercentageSumForDate(date: LocalDate): Int?

    @Query("SELECT SUM(currentPercentage) FROM tasks WHERE createdDate = :date")
    suspend fun getCompletedPercentageSumForDate(date: LocalDate): Int?
}