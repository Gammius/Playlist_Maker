package com.example.playlist_maker.presentation.mediaLibrary.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist_maker.domain.playlist.PlaylistInteractor
import com.example.playlist_maker.presentation.mediaLibrary.view_model.model.PlaylistsFragmentState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private val _playlist = MutableStateFlow(PlaylistsFragmentState())
    val playlist: StateFlow<PlaylistsFragmentState> get() = _playlist

    init {
        loadPlaylist()
    }

    fun loadPlaylist() {
        viewModelScope.launch {
            try {
                playlistInteractor.getAllPlaylist().collect { playlists ->
                    val newState = if (playlists.isEmpty()) {
                        PlaylistsFragmentState(noPlaylist = true)
                    } else {
                        PlaylistsFragmentState(playlists = playlists, noPlaylist = false)
                    }
                    _playlist.value = newState
                }
            } catch (e: Exception) {
            }
        }
    }
}