package com.mor.homeassignmenteat.buisness.datasource.network.main

import com.mor.homeassignmenteat.buisness.datasource.network.main.responses.StoryAResponse
import com.mor.homeassignmenteat.buisness.datasource.network.main.responses.StoryBResponse
import com.mor.homeassignmenteat.buisness.datasource.network.main.responses.StoryCResponse
import com.mor.homeassignmenteat.buisness.domain.util.Constants.Companion.DATA_SOURCE_A_URL
import com.mor.homeassignmenteat.buisness.domain.util.Constants.Companion.DATA_SOURCE_B_URL
import com.mor.homeassignmenteat.buisness.domain.util.Constants.Companion.DATA_SOURCE_C_URL
import retrofit2.http.GET

interface ApiMainService {
    @GET(DATA_SOURCE_A_URL)
    suspend fun getAResponse(): StoryAResponse?

    @GET(DATA_SOURCE_B_URL)
    suspend fun getBResponse(): StoryBResponse?

    @GET(DATA_SOURCE_C_URL)
    suspend fun getCResponse(): StoryCResponse?
}