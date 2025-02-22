package com.example.playlist_maker.presentation.mediaLibrary.view_model

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist_maker.domain.playlist.PlaylistInteractor
import com.example.playlist_maker.domain.playlist.model.Playlist
import com.example.playlist_maker.presentation.mediaLibrary.view_model.model.NewPlaylistState
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {
    private val _newPlaylistState = MutableLiveData(NewPlaylistState())
    val newPlaylistState: LiveData<NewPlaylistState> get() = _newPlaylistState

    fun onPlaylistNameChanged(namePlaylist: String) {
        _newPlaylistState.value = _newPlaylistState.value?.copy(
            namePlaylist = namePlaylist,
            isSendButtonEnabled = namePlaylist.isNotEmpty()
        )
    }

    fun onPlaylistDescriptionChanged(descriptionPlaylist: String) {
        _newPlaylistState.value = _newPlaylistState.value?.copy(
            descriptionPlaylist = descriptionPlaylist
        )
    }

    fun onImageUriChanged(uriImageCoverPlaylist: Uri) {
        _newPlaylistState.value = _newPlaylistState.value?.copy(
            uriImageCoverPlaylist = uriImageCoverPlaylist
        )
    }

    fun createNewPlaylist() {
        _newPlaylistState.value?.let { state ->
            if (state.namePlaylist.isNotEmpty()) {
                val playlist = Playlist(
                    id = 0,
                    namePlaylist = state.namePlaylist,
                    descriptionPlaylist = state.descriptionPlaylist,
                    uriImageCoverPlaylist = state.uriImageCoverPlaylist,
                    trackIds = emptyList(),
                    trackCount = 0
                )
                viewModelScope.launch {
                    playlistInteractor.addPlaylist(playlist)
                }
            }
        }
    }
}