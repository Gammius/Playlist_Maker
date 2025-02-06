package com.example.playlist_maker.domain.audioPlayer.impl

import com.example.playlist_maker.domain.audioPlayer.AudioPlayerRepository
import com.example.playlist_maker.domain.audioPlayer.AudioPlayerInteractor
import com.example.playlist_maker.domain.audioPlayer.model.AudioPlayerEvent
import kotlinx.coroutines.flow.Flow

class AudioPlayerInteractorImpl (
    private val repository: AudioPlayerRepository
) : AudioPlayerInteractor {

    override suspend fun preparePlayer(previewUrl: String) : Flow<AudioPlayerEvent> {
        return repository.preparePlayer(previewUrl)
    }

    override suspend fun startPlayer(): Flow<AudioPlayerEvent> {
        return repository.startPlayer()
    }

    override suspend fun pausePlayer(): Flow<AudioPlayerEvent> {
        return repository.pausePlayer()
    }

    override suspend fun resetPlayer(): Flow<AudioPlayerEvent> {
        return repository.resetPlayer()
    }

    override suspend fun getCurrentPosition(): Int {
       return repository.getCurrentPosition()
    }
}