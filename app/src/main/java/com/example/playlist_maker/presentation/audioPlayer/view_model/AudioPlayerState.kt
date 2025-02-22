package com.example.playlist_maker.presentation.audioPlayer.view_model

import com.example.playlist_maker.domain.playlist.model.Playlist

data class AudioPlayerState(
    val playerState: Int = 0,
    val currentTime: Long = 0L,
    val playButtonVisible: Boolean = true,
    val pauseButtonVisible: Boolean = false,
    val playlists: List<Playlist> = emptyList()
)
