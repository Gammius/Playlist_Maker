package com.example.playlist_maker.data.converters

import android.net.Uri
import com.example.playlist_maker.data.db.entity.PlaylistEntity
import com.example.playlist_maker.domain.playlist.model.Playlist
import com.google.gson.Gson

class PlaylistDbConvertor {

    fun map(playlist: Playlist): PlaylistEntity {
        val trackIdsString = Gson().toJson(playlist.trackIds)
        val imageUriString = playlist.uriImageCoverPlaylist.toString()

        return PlaylistEntity(
            id = playlist.id,
            namePlaylist = playlist.namePlaylist,
            descriptionPlaylist = playlist.descriptionPlaylist,
            uriImageCoverPlaylist = imageUriString,
            trackIds = trackIdsString,
            trackCount = playlist.trackCount
        )
    }

    fun map(playlistEntity: PlaylistEntity): Playlist {
        val trackIds = Gson().fromJson(playlistEntity.trackIds, Array<Long>::class.java).toList()
        val imageUri = playlistEntity.uriImageCoverPlaylist?.let { Uri.parse(it) }

        return Playlist(
            id = playlistEntity.id,
            namePlaylist = playlistEntity.namePlaylist,
            descriptionPlaylist = playlistEntity.descriptionPlaylist,
            uriImageCoverPlaylist = imageUri,
            trackIds = trackIds,
            trackCount = playlistEntity.trackCount
        )
    }
}