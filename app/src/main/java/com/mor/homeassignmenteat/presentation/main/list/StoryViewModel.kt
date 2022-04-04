package com.mor.homeassignmenteat.presentation.main.list

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.mor.homeassignmenteat.buisness.datasource.cache.worker.DeleteWorker
import com.mor.homeassignmenteat.buisness.domain.util.ErrorHandling
import com.mor.homeassignmenteat.buisness.domain.util.StateMessage
import com.mor.homeassignmenteat.buisness.domain.util.UIComponentType
import com.mor.homeassignmenteat.buisness.domain.util.doesMessageAlreadyExistInQueue
import dagger.hilt.android.lifecycle.HiltViewModel
import com.mor.homeassignmenteat.buisness.datasource.datastore.AppDataStore
import com.mor.homeassignmenteat.buisness.interactors.SearchStories
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

@HiltViewModel
class StoryViewModel
@Inject
constructor(
    private val stories: SearchStories,
    private val appDataStoreManager: AppDataStore,
) : ViewModel() {

    private val TAG: String = "AppDebug"

    val state: MutableLiveData<StoryState> = MutableLiveData(StoryState())

    init {
        onTriggerEvent(StoryEvents.NewSearch)
        Log.e(TAG, "mor state: ${state.value}", )
    }

    fun onTriggerEvent(event: StoryEvents) {
        when (event) {

            is StoryEvents.NewSearch -> {
                Log.e(TAG, "mor event: ${event}", )
                search()
            }

            is StoryEvents.Error -> {
                appendToMessageQueue(event.stateMessage)
            }
            is StoryEvents.OnRemoveHeadFromQueue -> {
                removeHeadFromQueue()
            }
            else -> {
                Log.e(TAG, "onTriggerEvent: $event")
            }
        }
    }


    private fun removeHeadFromQueue() {
        state.value?.let { state ->
            try {
                val queue = state.queue
                queue.remove() // can throw exception if empty
                this.state.value = state.copy(queue = queue)
            } catch (e: Exception) {
                Log.d(TAG, "removeHeadFromQueue: Nothing to remove from DialogQueue")
            }
        }
    }

    private fun appendToMessageQueue(stateMessage: StateMessage){
        state.value?.let { state ->
            val queue = state.queue
            if(!stateMessage.doesMessageAlreadyExistInQueue(queue = queue)){
                if(!(stateMessage.response.uiComponentType is UIComponentType.None)){
                    queue.add(stateMessage)
                    this.state.value = state.copy(queue = queue)
                }
            }
        }
    }

    private fun onUpdateQueryExhausted(isExhausted: Boolean) {
        state.value?.let { state ->
            this.state.value = state.copy(isQueryExhausted = isExhausted)
        }
    }

    private fun clearList() {
        state.value?.let { state ->
            this.state.value = state.copy(storyList = listOf())
        }
    }

    private fun resetPage() {
        state.value = state.value?.copy(page = 1)
        onUpdateQueryExhausted(false)
    }

    private fun incrementPageNumber() {
        state.value?.let { state ->
            this.state.value = state.copy(page = state.page + 1)
        }
    }

    private fun onUpdateQuery(query: String) {
        state.value = state.value?.copy(query = query)
    }

    private fun search() {
        resetPage()
        clearList()
        state.value?.let { state ->
            stories.execute().onEach { dataState ->

                Log.e(TAG, "search: $dataState" )
                this.state.value = state.copy(isLoading = dataState.isLoading)

                dataState.data?.let { list ->
                    this.state.value = state.copy(storyList = list)
                }

                dataState.stateMessage?.let { stateMessage ->
                    if(stateMessage.response.message?.contains(ErrorHandling.INVALID_PAGE) == true){
                        onUpdateQueryExhausted(true)
                    }else{
                        appendToMessageQueue(stateMessage)
                    }
                }

            }.launchIn(viewModelScope)
        }

    }



}
















