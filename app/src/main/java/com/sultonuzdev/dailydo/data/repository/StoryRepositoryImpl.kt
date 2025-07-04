package com.sultonuzdev.dailydo.data.repository

import com.sultonuzdev.dailydo.data.local.dao.StoryDao
import com.sultonuzdev.dailydo.data.local.entity.StoryEntity
import com.sultonuzdev.dailydo.domain.model.Story
import com.sultonuzdev.dailydo.domain.model.StoryCategory
import com.sultonuzdev.dailydo.domain.repository.StoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoryRepositoryImpl @Inject constructor(
    private val storyDao: StoryDao
) : StoryRepository {

    override fun getAllStories(): Flow<List<Story>> {
        return storyDao.getAllStories().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getStoriesByCategory(category: StoryCategory): Flow<List<Story>> {
        return storyDao.getStoriesByCategory(StoryCategory.toInt(category)).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getStoryById(id: Long): Flow<Story?> {
        return storyDao.getStoryById(id).map { entity ->
            entity?.toDomain()
        }
    }

    override suspend fun upsertStory(story: Story): Long {
        val entity = StoryEntity.fromDomain(story)
        return storyDao.insertStory(entity)
    }

    override suspend fun deleteStory(story: Story) {
        val entity = StoryEntity.fromDomain(story)
        storyDao.deleteStory(entity)
    }

    override suspend fun likeStory(storyId: Long) {
        storyDao.incrementLikes(storyId)
    }

    override fun getUserCreatedStories(): Flow<List<Story>> {
        return storyDao.getUserCreatedStories().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun searchStories(query: String): Flow<List<Story>> {
        return storyDao.searchStories(query).map { entities ->
            entities.map { it.toDomain() }
        }
    }
}