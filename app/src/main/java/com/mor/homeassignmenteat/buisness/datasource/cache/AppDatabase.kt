package com.mor.homeassignmenteat.buisness.datasource.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mor.homeassignmenteat.buisness.datasource.cache.story.StoryDao
import com.mor.homeassignmenteat.buisness.datasource.cache.story.StoryEntity


@Database(entities = [StoryEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getStoryDao(): StoryDao

    companion object{
        val DATABASE_NAME: String = "app_db"
    }

}








