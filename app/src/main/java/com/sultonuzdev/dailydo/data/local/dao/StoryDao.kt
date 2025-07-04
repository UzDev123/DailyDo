package com.sultonuzdev.dailydo.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sultonuzdev.dailydo.data.local.entity.StoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StoryDao {
    @Query("SELECT * FROM stories ORDER BY createdDate DESC")
    fun getAllStories(): Flow<List<StoryEntity>>

    @Query("SELECT * FROM stories WHERE category = :category ORDER BY createdDate DESC")
    fun getStoriesByCategory(category: Int): Flow<List<StoryEntity>>

    @Query("SELECT * FROM stories WHERE id = :id")
    fun getStoryById(id: Long): Flow<StoryEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: StoryEntity): Long

    @Update
    suspend fun updateStory(story: StoryEntity)

    @Delete
    suspend fun deleteStory(story: StoryEntity)

    @Query("UPDATE stories SET likes = likes + 1 WHERE id = :storyId")
    suspend fun incrementLikes(storyId: Long)

    @Query("SELECT * FROM stories WHERE isUserCreated = 1 ORDER BY createdDate DESC")
    fun getUserCreatedStories(): Flow<List<StoryEntity>>

    @Query("SELECT * FROM stories WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%'")
    fun searchStories(query: String): Flow<List<StoryEntity>>
}