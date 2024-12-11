package com.example.playlist_maker.presentation.audioPlayer.view_model

data class AudioPlayerState(
    val playerState: Int = 0,
    val currentTime: Long = 0L,
    val playButtonVisible: Boolean = true,
    val pauseButtonVisible: Boolean = false,
)
