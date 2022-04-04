package com.mor.homeassignmenteat.presentation.main.list

import com.mor.homeassignmenteat.buisness.domain.util.StateMessage


sealed class StoryEvents {

    object NewSearch : StoryEvents()

    object NextPage: StoryEvents()

    data class UpdateQuery(val query: String): StoryEvents()

    object GetOrderAndFilter: StoryEvents()

    data class Error(val stateMessage: StateMessage): StoryEvents()

    object OnRemoveHeadFromQueue: StoryEvents()
}
