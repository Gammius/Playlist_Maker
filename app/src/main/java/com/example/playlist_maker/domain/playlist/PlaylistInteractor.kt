package com.example.playlist_maker.domain.playlist

import android.net.Uri
import com.example.playlist_maker.domain.playlist.model.Playlist
import com.example.playlist_maker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun addPlaylist(playlist: Playlist)
    suspend fun getAllPlaylist(): Flow<List<Playlist>>
    suspend fun updatePlayList(playlistId: Long, trackIds: List<Long>, trackCount: Int)
    suspend fun addTrackPlaylist(track: Track)
    suspend fun getPlaylistById(playlistId: Long): Flow<Playlist?>
    suspend fun getAllTracks(): Flow<List<Track>>
    suspend fun deleteTrackPlaylist(track: Track)
    suspend fun deletePlaylistById(playlistId: Long)
    suspend fun updateEditPlaylist(
        playlistId: Long,
        namePlaylist: String,
        descriptionPlaylist: String,
        uriImageCoverPlaylist: Uri?
    )
    suspend fun deleteTrackById(trackId: Long)
}