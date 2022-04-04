package com.mor.homeassignmenteat.di

import android.app.Application
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.mor.homeassignmenteat.buisness.datasource.cache.AppDatabase
import com.mor.homeassignmenteat.buisness.datasource.cache.AppDatabase.Companion.DATABASE_NAME
import com.mor.homeassignmenteat.buisness.datasource.cache.story.StoryDao
import com.mor.homeassignmenteat.buisness.datasource.datastore.AppDataStore
import com.mor.homeassignmenteat.buisness.datasource.datastore.AppDataStoreManager
import com.mor.homeassignmenteat.buisness.datasource.network.main.ApiMainService
import com.mor.homeassignmenteat.buisness.domain.util.Constants
import com.mor.homeassignmenteat.buisness.interceptors.NetworkInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule{

    @Singleton
    @Provides
    fun provideDataStoreManager(
        application: Application
    ): AppDataStore {
        return AppDataStoreManager(application)
    }

    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson {
        return GsonBuilder()
            .create()
    }


    @Singleton
    @Provides
    fun provideOkHttpBuilder(
        application: Application
    ): OkHttpClient {
        val cacheSize = 10 * 1024 * 1024.toLong()
        val cache = Cache(application.cacheDir,cacheSize)

        return OkHttpClient()
            .newBuilder()
//            .cache(cache)
//            .addInterceptor(provideNetworkInterceptor())
            .build()
    }


    @Singleton
    @Provides
    fun provideNetworkInterceptor(): NetworkInterceptor {
        return NetworkInterceptor(60)
    }

    @Singleton
    @Provides
    fun provideRetrofitBuilder(gsonBuilder: Gson, application: Application): Retrofit.Builder{
        return Retrofit.Builder()
//            .client(provideOkHttpBuilder(application))
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gsonBuilder)
            )
    }

    @Singleton
    @Provides
    fun provideAppDb(app: Application): AppDatabase {
        return Room
            .databaseBuilder(app, AppDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration() // get correct db version if schema changed
            .build()
    }



    @Singleton
    @Provides
    fun provideApiMainService(retrofitBuilder: Retrofit.Builder): ApiMainService {
        return retrofitBuilder
            .build()
            .create(ApiMainService::class.java)
    }

    @Singleton
    @Provides
    fun provideStoryDao(db: AppDatabase): StoryDao {
        return db.getStoryDao()
    }

}