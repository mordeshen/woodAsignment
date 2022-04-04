package com.mor.homeassignmenteat.presentation.main.list

import com.mor.homeassignmenteat.buisness.domain.util.Queue
import com.mor.homeassignmenteat.buisness.domain.util.StateMessage
import com.mor.homeassignmenteat.buisness.domain.model.Story

data class StoryState(
    val isLoading: Boolean = false,
    val storyList: List<Story> = listOf(),
    val query: String = "",
    val page: Int = 1,
    val isQueryExhausted: Boolean = false, // no more results available, prevent next page
    val queue: Queue<StateMessage> = Queue(mutableListOf()),
)
