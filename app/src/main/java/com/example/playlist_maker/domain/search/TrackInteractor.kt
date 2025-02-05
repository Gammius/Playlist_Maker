package com.example.playlist_maker.domain.search

import com.example.playlist_maker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

interface TrackInteractor {
    suspend fun search(query: String): Flow<List<Track>?>
}