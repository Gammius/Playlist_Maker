package com.example.playlist_maker.data.audioPlayer.impl

import android.media.MediaPlayer
import com.example.playlist_maker.domain.audioPlayer.AudioPlayerRepository
import com.example.playlist_maker.domain.audioPlayer.model.AudioPlayerEvent
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class AudioPlayerRepositoryImpl (
    private val mediaPlayer: MediaPlayer
) : AudioPlayerRepository {


    override suspend fun preparePlayer(previewUrl: String): Flow<AudioPlayerEvent> = callbackFlow {
        mediaPlayer.reset()
        mediaPlayer.setDataSource(previewUrl)
        mediaPlayer.prepareAsync()

        mediaPlayer.setOnPreparedListener {
            trySend(AudioPlayerEvent.Prepared)
        }

        mediaPlayer.setOnCompletionListener {
            trySend(AudioPlayerEvent.Completed)
        }

        awaitClose { mediaPlayer.release() }
    }

    override suspend fun startPlayer(): Flow<AudioPlayerEvent> = callbackFlow {
        mediaPlayer.start()
        trySend(AudioPlayerEvent.Started)
        awaitClose { mediaPlayer.release() }
    }

    override suspend fun pausePlayer(): Flow<AudioPlayerEvent> = callbackFlow {
        mediaPlayer.pause()
        trySend(AudioPlayerEvent.Paused)
        awaitClose { mediaPlayer.release() }
    }

    override suspend fun resetPlayer(): Flow<AudioPlayerEvent> = callbackFlow {
            mediaPlayer.reset()
            trySend(AudioPlayerEvent.Reset)
            awaitClose { mediaPlayer.release() }
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }
}