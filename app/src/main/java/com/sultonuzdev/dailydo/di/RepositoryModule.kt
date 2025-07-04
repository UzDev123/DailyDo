package com.sultonuzdev.dailydo.di

import com.sultonuzdev.dailydo.data.repository.EfficiencyRepositoryImpl
import com.sultonuzdev.dailydo.data.repository.StoryRepositoryImpl
import com.sultonuzdev.dailydo.data.repository.TaskRepositoryImpl
import com.sultonuzdev.dailydo.data.repository.UserPreferencesRepositoryImpl
import com.sultonuzdev.dailydo.domain.repository.EfficiencyRepository
import com.sultonuzdev.dailydo.domain.repository.StoryRepository
import com.sultonuzdev.dailydo.domain.repository.TaskRepository
import com.sultonuzdev.dailydo.domain.repository.UserPreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTaskRepository(
        taskRepositoryImpl: TaskRepositoryImpl
    ): TaskRepository

    @Binds
    @Singleton
    abstract fun bindStoryRepository(
        storyRepositoryImpl: StoryRepositoryImpl
    ): StoryRepository

    @Binds
    @Singleton
    abstract fun bindEfficiencyRepository(
        efficiencyRepositoryImpl: EfficiencyRepositoryImpl
    ): EfficiencyRepository

    @Binds
    @Singleton
    abstract fun bindUserPreferencesRepository(
        userPreferencesRepositoryImpl: UserPreferencesRepositoryImpl
    ): UserPreferencesRepository
}