package com.example.playlist_maker.data.dto

class TrackResponse(
    val results: List<TrackDto> = emptyList()
) : Response()