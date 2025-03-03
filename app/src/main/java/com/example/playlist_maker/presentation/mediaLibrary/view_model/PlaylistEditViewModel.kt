package com.example.playlist_maker.presentation.mediaLibrary.view_model

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlist_maker.domain.playlist.PlaylistInteractor
import kotlinx.coroutines.launch

class PlaylistEditViewModel(
    playlistInteractor: PlaylistInteractor
) : NewPlaylistViewModel(playlistInteractor) {

    private val _updateSuccess = MutableLiveData<Boolean>()
    val updateSuccess: LiveData<Boolean> get() = _updateSuccess

    fun initializePlaylistData(
        playlistName: String?,
        playlistDescription: String?,
        playlistCoverUri: String?
    ) {
        _newPlaylistState.value = _newPlaylistState.value?.copy(
            namePlaylist = playlistName.orEmpty(),
            descriptionPlaylist = playlistDescription.orEmpty(),
            uriImageCoverPlaylist = playlistCoverUri?.let { Uri.parse(it) }
        )
    }

    fun updateEditPlaylist(
        playlistId: Long,
        namePlaylist: String,
        descriptionPlaylist: String,
        uriImageCoverPlaylist: Uri?
    ) {
        viewModelScope.launch {
            playlistInteractor.updateEditPlaylist(
                playlistId,
                namePlaylist,
                descriptionPlaylist,
                uriImageCoverPlaylist
            )
            _updateSuccess.postValue(true)
        }
    }

    override fun onPlaylistNameChanged(namePlaylist: String) {
        _newPlaylistState.value = _newPlaylistState.value?.copy(
            namePlaylist = namePlaylist,
            isSendButtonEnabled = namePlaylist.isNotEmpty()
        )
    }

    override fun onPlaylistDescriptionChanged(descriptionPlaylist: String) {
        _newPlaylistState.value = _newPlaylistState.value?.copy(
            descriptionPlaylist = descriptionPlaylist
        )
    }

    override fun onImageUriChanged(uriImageCoverPlaylist: Uri) {
        _newPlaylistState.value = _newPlaylistState.value?.copy(
            uriImageCoverPlaylist = uriImageCoverPlaylist
        )
    }
}