package com.example.playlist_maker.data.favoriteTrack.impl

import com.example.playlist_maker.data.converters.TrackDbConvertor
import com.example.playlist_maker.data.db.dao.TrackDao
import com.example.playlist_maker.data.db.entity.TrackEntity
import com.example.playlist_maker.domain.favoriteTrack.FavoriteTrackRepository
import com.example.playlist_maker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTrackRepositoryImpl(
    private val trackDao: TrackDao,
    private val trackDbConvertor: TrackDbConvertor
) : FavoriteTrackRepository {

    override suspend fun addTrackToFavorite(track: Track) {
        val trackDb = convertToTrackEntity(track)
        trackDao.insertNewTrack(trackDb)
    }

    override suspend fun removeTrackFromFavorite(track: Track) {
        val trackDb = convertToTrackEntity(track)
        trackDao.deleteTrackEntity(trackDb)
    }

    override suspend fun getTrackFavorite(): Flow<List<Track>> = flow {
        val tracks = trackDao.getTrackFavorite()
        emit(convertFromTrackEntity(tracks))
    }

    override suspend fun getTrackFavoriteIds(trackId: Long): Boolean {
        val favoriteIds = trackDao.getTrackFavoriteIds()
        return favoriteIds.contains(trackId)
    }

    private fun convertFromTrackEntity(trackDb: List<TrackEntity>): List<Track> {
        return trackDb.map { track -> trackDbConvertor.map(track) }
    }

    private fun convertToTrackEntity(track: Track): TrackEntity {
        val addedAt = System.currentTimeMillis()
        return trackDbConvertor.map(track, addedAt)
    }
}