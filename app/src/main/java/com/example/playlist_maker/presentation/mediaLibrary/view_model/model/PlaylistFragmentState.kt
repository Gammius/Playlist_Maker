package com.example.playlist_maker.presentation.mediaLibrary.view_model.model

import com.example.playlist_maker.domain.playlist.model.Playlist

data class PlaylistFragmentState(
    val noPlaylist: Boolean = true,
    val playlists: List<Playlist> = emptyList()
)