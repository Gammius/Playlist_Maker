package com.example.playlist_maker.domain.api

import com.example.playlist_maker.domain.models.Track

interface TrackRepository {
    fun search(query: String): List<Track>
}