package com.example.playlist_maker.presentation.mediaLibrary.view_model.model

import com.example.playlist_maker.domain.playlist.model.Playlist
import com.example.playlist_maker.domain.search.model.Track

data class PlaylistFragmentState(
    val trackPlaylist: List<Track> = emptyList(),
    val playlist: Playlist? = null,
    val totalDuration: Int = 0,
    val noTrackPlaylist: Boolean = false
)