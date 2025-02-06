package com.example.playlist_maker.domain.audioPlayer

import com.example.playlist_maker.domain.audioPlayer.model.AudioPlayerEvent
import kotlinx.coroutines.flow.Flow

interface AudioPlayerInteractor {
    suspend fun preparePlayer(previewUrl: String): Flow<AudioPlayerEvent>
    suspend fun startPlayer(): Flow<AudioPlayerEvent>
    suspend fun pausePlayer(): Flow<AudioPlayerEvent>
    suspend fun resetPlayer(): Flow<AudioPlayerEvent>
    suspend fun getCurrentPosition(): Int
}