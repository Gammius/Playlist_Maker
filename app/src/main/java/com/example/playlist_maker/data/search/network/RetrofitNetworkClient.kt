package com.example.playlist_maker.data.search.network

import com.example.playlist_maker.data.search.NetworkClient
import com.example.playlist_maker.data.search.dto.TrackRequest
import com.example.playlist_maker.data.search.dto.TrackResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetrofitNetworkClient(private val trackService: TrackApi) : NetworkClient  {
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