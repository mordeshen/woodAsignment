package com.mor.homeassignmenteat.buisness.interactors

import android.util.Log
import com.mor.homeassignmenteat.buisness.domain.util.DataState
import com.mor.homeassignmenteat.buisness.domain.util.MessageType
import com.mor.homeassignmenteat.buisness.domain.util.Response
import com.mor.homeassignmenteat.buisness.domain.util.UIComponentType
import com.mor.homeassignmenteat.buisness.datasource.cache.story.StoryDao
import com.mor.homeassignmenteat.buisness.datasource.cache.story.toEntity
import com.mor.homeassignmenteat.buisness.datasource.cache.story.toStory
import com.mor.homeassignmenteat.buisness.datasource.network.main.ApiMainService
import com.mor.homeassignmenteat.buisness.datasource.network.main.dto.toStory
import com.mor.homeassignmenteat.buisness.datasource.network.util.handleUseCaseException
import com.mor.homeassignmenteat.buisness.domain.model.Story
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class SearchStories(
    private val service: ApiMainService,
    private val cache: StoryDao,
) {

    private val TAG: String = "AppDebug"

    fun execute(): Flow<DataState<List<Story>>> = flow {
        emit(DataState.loading<List<Story>>())
        // get stories from network
        try{ // catch network exception
            val aStories = service.getAResponse()!!.stories.map { it.toStory() }
            val bStories = service.getBResponse()!!.metadata.data.map { it.toStory() }
            val cStories = service.getCResponse()!!.stories.map { it.toStory() }

            Log.e(TAG, "execute: $aStories" )

            // Insert into cache
            for(story in aStories){
                try{
                    cache.insert(story.toEntity(1))
                    //here insert to Alarm Manager with the exact time to delete from the cache

                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
//             Insert into cache
            for(story in bStories){
                try{
                    cache.insert(story.toEntity(2))
                    //here insert to Alarm Manager with the exact time to delete from the cache

                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
//            // Insert into cache
            for(story in cStories){
                try{
                    cache.insert(story.toEntity(3))
                    //here insert to Alarm Manager with the exact time to delete from the cache

                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }catch (e: Exception){
            emit(
                DataState.error<List<Story>>(
                    response = Response(
                        message = "Unable to update the cache.",
                        uiComponentType = UIComponentType.None(),
                        messageType = MessageType.Error()
                    )
                )
            )
        }

        // emit from cache
        val cachedStories = cache.getAllStories().map { it.toStory() }

        emit(DataState.data(response = null, data = cachedStories))
    }.catch { e ->
        emit(handleUseCaseException(e))
    }
}



















