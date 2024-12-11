package com.example.playlist_maker.data.audioPlayer

interface AudioPlayerRepository {
    fun preparePlayer(previewUrl: String, onPrepared: () -> Unit, onCompletion: () -> Unit)
    fun startPlayer()
    fun pausePlayer()
    fun resetPlayer()
    fun getCurrentPosition(): Int
}