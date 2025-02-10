package com.example.playlist_maker.data.search.network

import com.example.playlist_maker.data.search.NetworkClient
import com.example.playlist_maker.data.search.dto.TrackRequest
import com.example.playlist_maker.data.search.dto.TrackResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RetrofitNetworkClient(private val trackService: TrackApi) : NetworkClient {

    override suspend fun doRequest(dto: Any): Flow<TrackResponse> = flow {
        if (dto is TrackRequest) {
            try {
                val response = trackService.search(dto.query)
                val trackResponse = response.copy(resultCode = 200)
                emit(trackResponse)

            } catch (e: Throwable) {
                val errorResponse = TrackResponse(resultCode = 400)
                emit(errorResponse)
            }
        }
    }
}