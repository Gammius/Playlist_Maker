package com.example.playlist_maker.data.search

import com.example.playlist_maker.data.search.dto.TrackResponse

interface NetworkClient {
    fun doRequest(dto: Any, callback: (TrackResponse) -> Unit)
}