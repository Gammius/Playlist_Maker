package com.example.playlist_maker.data.converters

import com.example.playlist_maker.data.db.entity.TrackPlaylistEntity
import com.example.playlist_maker.domain.search.model.Track

class TrackPlaylistDbConvertor {
    fun map(track: Track, addedAt: Long): TrackPlaylistEntity {
        return TrackPlaylistEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            track.isFavorite,
            addedAt
        )
    }

    fun map(trackDb: TrackPlaylistEntity): Track {
        return Track(
            trackDb.trackId,
            trackDb.trackName,
            trackDb.artistName,
            trackDb.trackTimeMillis,
            trackDb.artworkUrl100,
            trackDb.collectionName,
            trackDb.releaseDate,
            trackDb.primaryGenreName,
            trackDb.country,
            trackDb.previewUrl,
            trackDb.isFavourite
        )
    }
}