package com.example.playlist_maker.domain.playlist

import com.example.playlist_maker.domain.playlist.model.Playlist
import com.example.playlist_maker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun addPlaylist(playlist: Playlist)
    suspend fun getAllPlaylist(): Flow<List<Playlist>>
    suspend fun updatePlayList(playlistId: Long, trackIds: List<Long>, trackCount: Int)
    suspend fun addTrackPlaylist(track: Track)
}