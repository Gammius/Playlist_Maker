package com.example.playlist_maker.data

import com.example.playlist_maker.data.dto.TrackResponse

interface NetworkClient {
    fun doRequest(dto: Any): TrackResponse
}