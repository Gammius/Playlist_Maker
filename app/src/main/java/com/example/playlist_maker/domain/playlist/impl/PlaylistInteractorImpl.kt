package com.example.playlist_maker.domain.playlist.impl

import android.net.Uri
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

    override suspend fun getPlaylistById(playlistId: Long): Flow<Playlist?> {
        return repository.getPlaylistById(playlistId)
    }

    override suspend fun getAllTracks(): Flow<List<Track>> {
        return repository.getAllTracks()
    }

    override suspend fun deleteTrackPlaylist(track: Track) {
        repository.deleteTrackPlaylist(track)
    }

    override suspend fun deletePlaylistById(playlistId: Long) {
        repository.deletePlaylistById(playlistId)
    }

    override suspend fun updateEditPlaylist(
        playlistId: Long,
        namePlaylist: String,
        descriptionPlaylist: String,
        uriImageCoverPlaylist: Uri?
    ) {
        repository.updateEditPlaylist(
            playlistId,
            namePlaylist,
            descriptionPlaylist,
            uriImageCoverPlaylist
        )
    }

    override suspend fun deleteTrackById(trackId: Long) {
        repository.deleteTrackById(trackId)
    }
}