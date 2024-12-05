package com.example.playlist_maker.data.network

import com.example.playlist_maker.data.NetworkClient
import com.example.playlist_maker.data.dto.Response
import com.example.playlist_maker.data.dto.TrackRequest
import com.example.playlist_maker.data.dto.TrackResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {

    private val trackBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(trackBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val trackService = retrofit.create(trackApi::class.java)

    override fun doRequest(dto: Any): TrackResponse {
        if (dto is TrackRequest) {
            val resp = trackService.search(dto.query).execute()
            val body = resp.body() ?: TrackResponse()
            return body.apply { resultCode = resp.code() }
        } else {
            return TrackResponse().apply { resultCode = 400 }
        }
    }
}