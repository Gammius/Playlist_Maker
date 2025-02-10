package com.example.playlist_maker.data.search.network

import com.example.playlist_maker.data.search.dto.TrackResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TrackApi {
    @GET("/search?entity=song")
    suspend fun search(
        @Query("term") query: String
    ): TrackResponse
}