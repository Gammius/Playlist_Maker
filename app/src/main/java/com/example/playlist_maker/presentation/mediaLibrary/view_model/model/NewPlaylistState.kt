package com.example.playlist_maker.presentation.mediaLibrary.view_model.model

import android.net.Uri

data class NewPlaylistState(
    val namePlaylist: String = "",
    val descriptionPlaylist: String = "",
    val uriImageCoverPlaylist: Uri? = null,
    val isSendButtonEnabled: Boolean = false
)