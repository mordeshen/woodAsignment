package com.mor.homeassignmenteat.buisness.datasource.network.main.responses

import com.google.gson.annotations.SerializedName
import com.mor.homeassignmenteat.buisness.datasource.network.main.dto.StoryCDTO
import com.mor.homeassignmenteat.buisness.datasource.network.main.dto.toStory
import com.mor.homeassignmenteat.buisness.domain.model.Story

class StoryCResponse (
    @SerializedName("stories")
    var stories: List<StoryCDTO>

    )

fun StoryCResponse.toList(): List<Story>{
    val list: MutableList<Story> = mutableListOf()
    for(dto in stories){
        list.add(
            dto.toStory()
        )
    }
    return list
}