package com.example.playlist_maker.presentation.mediaLibrary.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist_maker.domain.playlist.PlaylistInteractor
import com.example.playlist_maker.presentation.mediaLibrary.view_model.model.PlaylistFragmentState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private val _playlist = MutableStateFlow(PlaylistFragmentState())
    val playlist: StateFlow<PlaylistFragmentState> get() = _playlist

    init {
        loadPlaylist()
    }

    fun loadPlaylist() {
        viewModelScope.launch {
            try {
                playlistInteractor.getAllPlaylist().collect { playlists ->
                    val newState = if (playlists.isEmpty()) {
                        PlaylistFragmentState(noPlaylist = true)
                    } else {
                        PlaylistFragmentState(playlists = playlists, noPlaylist = false)
                    }
                    _playlist.value = newState
                }
            } catch (e: Exception) {
            }
        }
    }
}