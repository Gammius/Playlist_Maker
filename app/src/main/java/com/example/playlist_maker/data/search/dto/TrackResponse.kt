package com.example.playlist_maker.data.search.dto

data class TrackResponse(
    val results: List<TrackDto> = emptyList(),
    val resultCode: Int = 0
) : Response()