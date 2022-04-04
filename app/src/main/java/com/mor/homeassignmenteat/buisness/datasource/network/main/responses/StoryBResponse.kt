package com.mor.homeassignmenteat.buisness.datasource.network.main.responses

import com.google.gson.annotations.SerializedName
import com.mor.homeassignmenteat.buisness.datasource.network.main.dto.StoryBDTO
import com.mor.homeassignmenteat.buisness.datasource.network.main.dto.toStory
import com.mor.homeassignmenteat.buisness.domain.model.Story

data class StoryBResponse(
    val metadata: MyMetadata
)

data class MyMetadata(
    @SerializedName("this")
    val _this: String,

    @SerializedName("innerdata")
    val data: List<StoryBDTO>
)

fun StoryBResponse.toList(): List<Story>{
    val list: MutableList<Story> = mutableListOf()
    for(dto in metadata.data){
        list.add(
            dto.toStory()
        )
    }
    return list
}