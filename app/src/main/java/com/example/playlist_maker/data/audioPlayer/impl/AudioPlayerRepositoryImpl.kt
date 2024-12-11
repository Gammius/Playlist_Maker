package com.example.playlist_maker.data.audioPlayer.impl

import android.media.MediaPlayer
import com.example.playlist_maker.data.audioPlayer.AudioPlayerRepository

class AudioPlayerRepositoryImpl (
    private val mediaPlayer: MediaPlayer
) : AudioPlayerRepository {


    override fun preparePlayer(previewUrl: String,onPrepared: () -> Unit, onCompletion: () -> Unit) {
        mediaPlayer.reset()
        mediaPlayer.setDataSource(previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            onPrepared()
        }
        mediaPlayer.setOnCompletionListener {
            onCompletion()
        }
    }

    override fun startPlayer() {
            mediaPlayer.start()
    }

    override fun pausePlayer() {
            mediaPlayer.pause()
        }

    override fun resetPlayer() {
        mediaPlayer.reset()
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }
}