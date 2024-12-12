package com.example.playlist_maker.domain.search

import com.example.playlist_maker.domain.search.model.Track

interface TrackInteractor {
    fun search(query: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(trackList: List<Track>?)
    }
}