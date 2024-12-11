package com.example.playlist_maker.domain.audioPlayer

interface AudioPlayerInteractor {
    fun preparePlayer(previewUrl: String,onPrepared: () -> Unit, onCompletion: () -> Unit)
    fun startPlayer()
    fun pausePlayer()
    fun resetPlayer()
    fun getCurrentPosition(): Int
}