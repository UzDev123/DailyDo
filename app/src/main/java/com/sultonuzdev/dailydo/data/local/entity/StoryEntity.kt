package com.sultonuzdev.dailydo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sultonuzdev.dailydo.domain.model.Story
import com.sultonuzdev.dailydo.domain.model.StoryCategory
import org.threeten.bp.LocalDateTime

@Entity(tableName = "stories")
data class StoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
    val author: String,
    val category: Int,
    val createdDate: LocalDateTime,
    val imageUrl: String?,
    val likes: Int,
    val isUserCreated: Boolean
) {
    companion object {
        fun fromDomain(story: Story): StoryEntity {
            return StoryEntity(
                id = story.id,
                title = story.title,
                content = story.content,
                author = story.author,
                category = StoryCategory.toInt(story.category),
                createdDate = story.createdDate,
                imageUrl = story.imageUrl,
                likes = story.likes,
                isUserCreated = story.isUserCreated
            )
        }
    }

    fun toDomain(): Story {
        return Story(
            id = id,
            title = title,
            content = content,
            author = author,
            category = StoryCategory.fromInt(category),
            createdDate = createdDate,
            imageUrl = imageUrl,
            likes = likes,
            isUserCreated = isUserCreated
        )
    }
}