package com.sultonuzdev.dailydo.domain.model

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

/**
 * Represents a task in the Daily Do app.
 */
data class Task(
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val targetPercentage: Int, // Target completion percentage
    val currentPercentage: Int = 0, // Current completion percentage
    val createdDate: LocalDate = LocalDate.now(),
    val deadline: LocalDateTime? = null,
    val isCompleted: Boolean = false,
    val priority: Priority = Priority.MEDIUM
)

/**
 * Represents the priority levels for tasks.
 */
enum class Priority {
    LOW, MEDIUM, HIGH;

    companion object {
        fun fromInt(value: Int) = when (value) {
            0 -> LOW
            1 -> MEDIUM
            2 -> HIGH
            else -> MEDIUM
        }

        fun toInt(priority: Priority) = when (priority) {
            LOW -> 0
            MEDIUM -> 1
            HIGH -> 2
        }
    }
}