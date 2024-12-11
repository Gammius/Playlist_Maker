package com.example.playlist_maker.data.search

import com.example.playlist_maker.domain.search.model.Track

interface TrackRepository {
    fun search(query: String, callback: (List<Track>, Int) -> Unit)
}