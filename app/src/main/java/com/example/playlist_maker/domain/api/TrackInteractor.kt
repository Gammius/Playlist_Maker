package com.example.playlist_maker.domain.api

import com.example.playlist_maker.domain.models.Track

interface TrackInteractor {
    fun search(query: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(trackList: List<Track>)
    }
}