package com.sultonuzdev.dailydo.di

import android.content.Context
import androidx.room.Room
import com.sultonuzdev.dailydo.data.local.Converters
import com.sultonuzdev.dailydo.data.local.DailyDoDatabase
import com.sultonuzdev.dailydo.data.local.dao.DailyEfficiencyDao
import com.sultonuzdev.dailydo.data.local.dao.StoryDao
import com.sultonuzdev.dailydo.data.local.dao.TaskDao
import com.sultonuzdev.dailydo.data.local.dao.UserPreferencesDao
import com.sultonuzdev.dailydo.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideConverters(): Converters {
        return Converters()
    }

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        converters: Converters
    ): DailyDoDatabase {
        return Room.databaseBuilder(
            context,
            DailyDoDatabase::class.java,
            Constants.DB_NAME
        )
            .addTypeConverter(converters)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideTaskDao(database: DailyDoDatabase): TaskDao {
        return database.taskDao()
    }

    @Provides
    @Singleton
    fun provideStoryDao(database: DailyDoDatabase): StoryDao {
        return database.storyDao()
    }

    @Provides
    @Singleton
    fun provideDailyEfficiencyDao(database: DailyDoDatabase): DailyEfficiencyDao {
        return database.dailyEfficiencyDao()
    }

    @Provides
    @Singleton
    fun provideUserPreferencesDao(database: DailyDoDatabase): UserPreferencesDao {
        return database.userPreferencesDao()
    }
}