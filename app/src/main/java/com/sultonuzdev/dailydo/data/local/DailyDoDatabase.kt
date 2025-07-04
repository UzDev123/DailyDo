package com.sultonuzdev.dailydo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sultonuzdev.dailydo.data.local.dao.DailyEfficiencyDao
import com.sultonuzdev.dailydo.data.local.dao.StoryDao
import com.sultonuzdev.dailydo.data.local.dao.TaskDao
import com.sultonuzdev.dailydo.data.local.dao.UserPreferencesDao
import com.sultonuzdev.dailydo.data.local.entity.DailyEfficiencyEntity
import com.sultonuzdev.dailydo.data.local.entity.StoryEntity
import com.sultonuzdev.dailydo.data.local.entity.TaskEntity
import com.sultonuzdev.dailydo.data.local.entity.UserPreferencesEntity

@Database(
    entities = [
        TaskEntity::class,
        StoryEntity::class,
        DailyEfficiencyEntity::class,
        UserPreferencesEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class DailyDoDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun storyDao(): StoryDao
    abstract fun dailyEfficiencyDao(): DailyEfficiencyDao
    abstract fun userPreferencesDao(): UserPreferencesDao


}