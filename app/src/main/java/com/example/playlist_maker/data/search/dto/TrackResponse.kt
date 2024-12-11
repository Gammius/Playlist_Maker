package com.example.playlist_maker.data.search.dto

class TrackResponse(
    val results: List<TrackDto> = emptyList()
) : Response()