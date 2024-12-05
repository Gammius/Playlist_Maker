package com.example.playlist_maker.data.network

import com.example.playlist_maker.data.dto.TrackResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface trackApi {
    @GET("/search?entity=song")
    fun search(
        @Query("term") query: String
    ): Call<TrackResponse>
}