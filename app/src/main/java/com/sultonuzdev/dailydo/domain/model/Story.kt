package com.sultonuzdev.dailydo.domain.model

import org.threeten.bp.LocalDateTime

/**
 * Represents a motivational story in the Daily Do app.
 */
data class Story(
    val id: Long = 0,
    val title: String,
    val content: String,
    val author: String = "",
    val category: StoryCategory,
    val createdDate: LocalDateTime = LocalDateTime.now(),
    val imageUrl: String? = null,
    val likes: Int = 0,
    val isUserCreated: Boolean = false
)

/**
 * Represents different categories of motivational stories.
 */
enum class StoryCategory {
    SUCCESS_STORY,
    PRODUCTIVITY_TIP,
    DAILY_MOTIVATION,
    LIFE_HACK,
    OTHER;

    companion object {
        fun fromInt(value: Int) = when (value) {
            0 -> SUCCESS_STORY
            1 -> PRODUCTIVITY_TIP
            2 -> DAILY_MOTIVATION
            3 -> LIFE_HACK
            4 -> OTHER
            else -> OTHER
        }

        fun toInt(category: StoryCategory) = when (category) {
            SUCCESS_STORY -> 0
            PRODUCTIVITY_TIP -> 1
            DAILY_MOTIVATION -> 2
            LIFE_HACK -> 3
            OTHER -> 4
        }

        fun displayName(category: StoryCategory) = when (category) {
            SUCCESS_STORY -> "Success Story"
            PRODUCTIVITY_TIP -> "Productivity Tip"
            DAILY_MOTIVATION -> "Daily Motivation"
            LIFE_HACK -> "Life Hack"
            OTHER -> "Other"
        }
    }
}