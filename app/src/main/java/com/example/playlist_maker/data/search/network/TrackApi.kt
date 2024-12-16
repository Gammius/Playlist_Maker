package com.example.playlist_maker.data.search.network

import com.example.playlist_maker.data.search.dto.TrackResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface trackApi {
    @GET("/search?entity=song")
    fun search(
        @Query("term") query: String
    ): Call<TrackResponse>
}