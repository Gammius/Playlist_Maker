package com.example.playlist_maker.data.search.network

import com.example.playlist_maker.data.search.NetworkClient
import com.example.playlist_maker.data.search.dto.TrackRequest
import com.example.playlist_maker.data.search.dto.TrackResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {

    private val trackBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(trackBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val trackService = retrofit.create(trackApi::class.java)

    override fun doRequest(dto: Any, callback: (TrackResponse) -> Unit) {
        if (dto is TrackRequest) {
            trackService.search(dto.query).enqueue(object : Callback<TrackResponse> {
                override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>) {
                    val body = response.body() ?: TrackResponse()
                    val result = body.apply { resultCode = response.code() }
                    callback(result)
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    val errorResponse = TrackResponse().apply { resultCode = 400 }
                    callback(errorResponse)
                }
            })
        }
    }
}