package com.example.playlist_maker.domain.audioPlayer.model

sealed class AudioPlayerEvent {
    object Prepared : AudioPlayerEvent()
    object Started : AudioPlayerEvent()
    object Paused : AudioPlayerEvent()
    object Completed : AudioPlayerEvent()
    object Reset : AudioPlayerEvent()
}