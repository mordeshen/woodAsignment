package com.mor.homeassignmenteat.di.story

import android.app.Application
import androidx.work.WorkerParameters
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.mor.homeassignmenteat.buisness.datasource.cache.story.StoryDao
import com.mor.homeassignmenteat.buisness.datasource.cache.worker.DeleteWorker
import com.mor.homeassignmenteat.buisness.datasource.network.main.ApiMainService
import com.mor.homeassignmenteat.buisness.interactors.SearchStories
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StoryModule {

    //currently ain't no need with submodule
    @Singleton
    @Provides
    fun provideSearchStories(
        service: ApiMainService,
        dao: StoryDao,
    ): SearchStories {
        return SearchStories(service, dao)
    }

    @Singleton
    @Provides
    fun provideDeleteStories(
        application: Application,
        dao: StoryDao,
        params: WorkerParameters
    ): DeleteWorker {
        return DeleteWorker(application, params = params, dao = dao)
    }
}

















