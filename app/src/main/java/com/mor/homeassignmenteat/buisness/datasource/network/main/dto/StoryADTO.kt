package com.mor.homeassignmenteat.buisness.datasource.network.main.dto

import com.google.gson.annotations.SerializedName
import com.mor.homeassignmenteat.buisness.domain.model.Story

/**
{
"stories": [
{
"title": "Interesting News!",
"subtitle": "You're not gonna believe this...",
"imageUrl": "https://pbs.twimg.com/profile_images/658218628127588352/v0ZLUBrt.jpg"
}
...
]
}
*/

class StoryADTO(
    @SerializedName("title")
    val title: String,

    @SerializedName("subtitle")
    val subtitle: String,

    @SerializedName("imageUrl")
    val imageUrl: String,
)

fun StoryADTO.toStory(): Story {
    return Story(
        title = title,
        subtitle = subtitle,
        imageUrl = imageUrl
    )
}

fun Story.toADto(): StoryADTO {
    return StoryADTO(
        title = title ?: "",
        subtitle = subtitle ?: "",
        imageUrl = imageUrl  ?: ""
    )
}
