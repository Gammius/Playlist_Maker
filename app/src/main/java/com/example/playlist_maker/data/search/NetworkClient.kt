package com.example.playlist_maker.data.search

import com.example.playlist_maker.data.search.dto.TrackResponse
import kotlinx.coroutines.flow.Flow

interface NetworkClient {
    suspend fun doRequest(dto: Any) : Flow<TrackResponse>
}