package com.example.playlist_maker.domain.playlist.model

import android.net.Uri

data class Playlist(
    val id: Long,
    val namePlaylist: String,
    val descriptionPlaylist: String,
    val uriImageCoverPlaylist: Uri?,
    val trackIds: List<Long>,
    val trackCount: Int
)