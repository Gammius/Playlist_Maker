package com.example.playlist_maker.domain.playlist.impl

import com.example.playlist_maker.domain.playlist.PlaylistInteractor
import com.example.playlist_maker.domain.playlist.PlaylistRepository
import com.example.playlist_maker.domain.playlist.model.Playlist
import com.example.playlist_maker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(
    private val repository: PlaylistRepository
) : PlaylistInteractor {

    override suspend fun addPlaylist(playlist: Playlist) {
        repository.addPlaylist(playlist)
    }

    override suspend fun getAllPlaylist(): Flow<List<Playlist>> {
        return repository.getAllPlaylist()
    }

    override suspend fun updatePlayList(playlistId: Long, trackIds: List<Long>, trackCount: Int) {
        repository.updatePlayList(playlistId, trackIds, trackCount)
    }

    override suspend fun addTrackPlaylist(track: Track) {
        repository.addTrackPlaylist(track)
    }
}