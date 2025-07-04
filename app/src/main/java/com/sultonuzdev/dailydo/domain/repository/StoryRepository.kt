package com.sultonuzdev.dailydo.domain.repository

import com.sultonuzdev.dailydo.domain.model.Story
import com.sultonuzdev.dailydo.domain.model.StoryCategory
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Story-related operations.
 */
interface StoryRepository {
    /**
     * Get all stories as a Flow.
     */
    fun getAllStories(): Flow<List<Story>>

    /**
     * Get stories by category as a Flow.
     */
    fun getStoriesByCategory(category: StoryCategory): Flow<List<Story>>

    /**
     * Get a story by its ID as a Flow.
     */
    fun getStoryById(id: Long): Flow<Story?>

    /**
     * Insert or update a story.
     * @return The ID of the inserted/updated story
     */
    suspend fun upsertStory(story: Story): Long

    /**
     * Delete a story.
     */
    suspend fun deleteStory(story: Story)

    /**
     * Like a story.
     */
    suspend fun likeStory(storyId: Long)

    /**
     * Get user-created stories as a Flow.
     */
    fun getUserCreatedStories(): Flow<List<Story>>

    /**
     * Search stories by query.
     */
    fun searchStories(query: String): Flow<List<Story>>
}