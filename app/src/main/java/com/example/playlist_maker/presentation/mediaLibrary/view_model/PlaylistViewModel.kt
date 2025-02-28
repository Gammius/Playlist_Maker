package com.example.playlist_maker.presentation.mediaLibrary.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist_maker.domain.playlist.PlaylistInteractor
import com.example.playlist_maker.domain.search.model.Track
import com.example.playlist_maker.presentation.mediaLibrary.view_model.model.PlaylistFragmentState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private val _playlist = MutableStateFlow(PlaylistFragmentState())
    val playlist: StateFlow<PlaylistFragmentState> get() = _playlist


    fun loadPlaylistAndTracks(playlistId: Long) {
        viewModelScope.launch {
            playlistInteractor.getPlaylistById(playlistId).collect { playlist ->
                playlistInteractor.getAllTracks().collect { tracks ->
                    val trackPlaylist =
                        tracks.filter { track -> playlist!!.trackIds.contains(track.trackId) }
                    val totalDurationMillis = trackPlaylist.sumOf { it.trackTimeMillis }
                    val durationMinutes =
                        SimpleDateFormat("mm", Locale.getDefault()).format(totalDurationMillis)
                            .toInt()
                    _playlist.value = PlaylistFragmentState(
                        playlist = playlist,
                        trackPlaylist = trackPlaylist,
                        totalDuration = durationMinutes,
                        noTrackPlaylist = trackPlaylist.isEmpty()
                    )
                }
            }
        }
    }

    fun deleteTrackPlaylist(playlistId: Long, track: Track) {
        viewModelScope.launch {
            playlistInteractor.getPlaylistById(playlistId).collect { playlist ->
                val updatedTrackIds = playlist!!.trackIds - track.trackId
                val updatedTrackCount = playlist.trackCount - 1
                playlistInteractor.updatePlayList(playlistId, updatedTrackIds, updatedTrackCount)
            }
            loadPlaylistAndTracks(playlistId)
            var isTrackInOtherPlaylists = false
            playlistInteractor.getAllPlaylist().collect { playlists ->
                isTrackInOtherPlaylists = playlists.any { playlist ->
                    playlist.trackIds.contains(track.trackId) && playlist.id != playlistId
                }
            }

            if (!isTrackInOtherPlaylists) {
                playlistInteractor.deleteTrackPlaylist(track)
            }
        }
    }

    fun deletePlaylist(playlistId: Long) {
        viewModelScope.launch {
            playlistInteractor.deletePlaylistById(playlistId)
        }
    }
}