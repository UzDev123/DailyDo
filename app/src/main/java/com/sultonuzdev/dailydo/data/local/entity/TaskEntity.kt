package com.sultonuzdev.dailydo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sultonuzdev.dailydo.domain.model.Priority
import com.sultonuzdev.dailydo.domain.model.Task
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val targetPercentage: Int,
    val currentPercentage: Int,
    val createdDate: LocalDate,
    val deadline: LocalDateTime?,
    val isCompleted: Boolean,
    val priority: Int // Mapped from Priority enum
) {
    companion object {
        fun fromDomain(task: Task): TaskEntity {
            return TaskEntity(
                id = task.id,
                title = task.title,
                description = task.description,
                targetPercentage = task.targetPercentage,
                currentPercentage = task.currentPercentage,
                createdDate = task.createdDate,
                deadline = task.deadline,
                isCompleted = task.isCompleted,
                priority = Priority.toInt(task.priority)
            )
        }
    }

    fun toDomain(): Task {
        return Task(
            id = id,
            title = title,
            description = description,
            targetPercentage = targetPercentage,
            currentPercentage = currentPercentage,
            createdDate = createdDate,
            deadline = deadline,
            isCompleted = isCompleted,
            priority = Priority.fromInt(priority)
        )
    }
}