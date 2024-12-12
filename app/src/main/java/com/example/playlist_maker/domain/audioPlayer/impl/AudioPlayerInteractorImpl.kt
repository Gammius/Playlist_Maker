package com.example.playlist_maker.domain.audioPlayer.impl

import com.example.playlist_maker.domain.audioPlayer.AudioPlayerRepository
import com.example.playlist_maker.domain.audioPlayer.AudioPlayerInteractor

class AudioPlayerInteractorImpl (
    private val repository: AudioPlayerRepository
) : AudioPlayerInteractor {

    override fun preparePlayer(previewUrl: String, onPrepared: () -> Unit, onCompletion: () -> Unit) {
        repository.preparePlayer(previewUrl,onPrepared,onCompletion)
    }

    override fun startPlayer() {
        repository.startPlayer()
    }

    override fun pausePlayer() {
        repository.pausePlayer()
    }

    override fun resetPlayer() {
        repository.resetPlayer()
    }

    override fun getCurrentPosition(): Int {
       return repository.getCurrentPosition()
    }
}