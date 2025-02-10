package com.example.playlist_maker.domain.search.impl

import com.example.playlist_maker.domain.search.TrackInteractor
import com.example.playlist_maker.domain.search.TrackRepository
import com.example.playlist_maker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

class TrackInteractorImpl(private val repository: TrackRepository) : TrackInteractor {

    override suspend fun search(query: String): Flow<List<Track>?> {
        return repository.search(query)
    }
}