package com.example.playlist_maker.domain.search.impl

import com.example.playlist_maker.domain.search.TrackInteractor
import com.example.playlist_maker.domain.search.TrackRepository

class TrackInteractorImpl(private val repository: TrackRepository) : TrackInteractor {

    override fun search(query: String, consumer: TrackInteractor.TrackConsumer) {
        repository.search(query) { trackList ->
            consumer.consume(trackList)
        }
    }
}