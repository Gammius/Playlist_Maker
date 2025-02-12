package com.example.playlist_maker.domain.favoriteTrack.impl

import com.example.playlist_maker.domain.favoriteTrack.FavoriteTrackInteractor
import com.example.playlist_maker.domain.favoriteTrack.FavoriteTrackRepository
import com.example.playlist_maker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

class FavoriteTrackInteractorImpl(
    private val repository: FavoriteTrackRepository
) : FavoriteTrackInteractor {

    override suspend fun addTrackToFavorite(track: Track) {
        repository.addTrackToFavorite(track)
    }

    override suspend fun removeTrackFromFavorite(track: Track) {
        repository.removeTrackFromFavorite(track)
    }

    override suspend fun getTrackFavorite(): Flow<List<Track>> {
        return repository.getTrackFavorite()
    }

    override suspend fun getTrackFavoriteIds(trackId: Long): Boolean {
        return repository.getTrackFavoriteIds(trackId)
    }
}