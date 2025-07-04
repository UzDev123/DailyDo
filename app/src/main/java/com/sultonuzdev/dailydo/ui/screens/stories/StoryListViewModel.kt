package com.sultonuzdev.dailydo.ui.screens.stories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sultonuzdev.dailydo.domain.model.Story
import com.sultonuzdev.dailydo.domain.model.StoryCategory
import com.sultonuzdev.dailydo.domain.repository.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Story List screen
 */
@HiltViewModel
class StoryListViewModel @Inject constructor(
    private val storyRepository: StoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StoryListUiState())
    val uiState: StateFlow<StoryListUiState> = _uiState.asStateFlow()

    private var selectedCategory: StoryCategory? = null

    init {
        loadStories()
    }

    /**
     * Load stories based on selected category
     */
    fun loadStories() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val storiesFlow = if (selectedCategory != null) {
                storyRepository.getStoriesByCategory(selectedCategory!!)
            } else {
                storyRepository.getAllStories()
            }

            storiesFlow.collect { stories ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        stories = stories.map { story -> story.toUiModel() }
                    )
                }
            }
        }
    }

    /**
     * Filter stories by category
     */
    fun filterByCategory(category: StoryCategory?) {
        selectedCategory = category
        loadStories()
    }

    /**
     * Like a story
     */
    fun likeStory(storyId: Long) {
        viewModelScope.launch {
            storyRepository.likeStory(storyId)
        }
    }

    /**
     * Convert Story domain model to UI model
     */
    private fun Story.toUiModel(): StoryUiModel {
        return StoryUiModel(
            id = id,
            title = title,
            content = content,
            author = author,
            categoryName = StoryCategory.displayName(category),
            imageUrl = imageUrl,
            likes = likes,
            isUserCreated = isUserCreated
        )
    }
}

/**
 * UI state for the Story List screen
 */
data class StoryListUiState(
    val isLoading: Boolean = false,
    val stories: List<StoryUiModel> = emptyList()
)

/**
 * UI model for a Story
 */
data class StoryUiModel(
    val id: Long,
    val title: String,
    val content: String,
    val author: String,
    val categoryName: String,
    val imageUrl: String? = null,
    val likes: Int,
    val isUserCreated: Boolean
)