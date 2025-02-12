package com.example.playlist_maker.domain.favoriteTrack

import com.example.playlist_maker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTrackInteractor {
    suspend fun addTrackToFavorite(track: Track)
    suspend fun removeTrackFromFavorite(track: Track)
    suspend fun getTrackFavorite(): Flow<List<Track>>
    suspend fun getTrackFavoriteIds(trackId: Long): Boolean
}