package com.example.playlist_maker.presentation.audioPlayer.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist_maker.domain.audioPlayer.AudioPlayerInteractor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AudioPlayerViewModel(
    private val audioPlayerInteractor: AudioPlayerInteractor
) : ViewModel() {

    companion object {
        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
    }

    private val _audioPlayerState = MutableLiveData<AudioPlayerState>()
    val audioPlayerState: LiveData<AudioPlayerState> get() = _audioPlayerState

    init {
        _audioPlayerState.value = AudioPlayerState()
    }

    fun preparePlayer(previewUrl: String) {
        audioPlayerInteractor.preparePlayer(previewUrl,
            onPrepared = {
                _audioPlayerState.value = _audioPlayerState.value?.copy(
                    playerState = STATE_PREPARED,
                    playButtonVisible = true,
                    pauseButtonVisible = false
                )
                startTimer()
            },
            onCompletion = {
                _audioPlayerState.value = _audioPlayerState.value?.copy(
                    playerState = STATE_PREPARED,
                    currentTime = 0L,
                    playButtonVisible = true,
                    pauseButtonVisible = false
                )
            })
    }

    fun startPlayer() {
        audioPlayerInteractor.startPlayer()
        _audioPlayerState.value = _audioPlayerState.value?.copy(
            playerState = STATE_PLAYING,
            playButtonVisible = false,
            pauseButtonVisible = true
        )
        startTimer()
    }

    fun pausePlayer() {
        audioPlayerInteractor.pausePlayer()
        _audioPlayerState.value = _audioPlayerState.value?.copy(
            playerState = STATE_PAUSED,
            playButtonVisible = true,
            pauseButtonVisible = false
        )
    }

    fun resetPlayer() {
        audioPlayerInteractor.resetPlayer()
        _audioPlayerState.value = _audioPlayerState.value?.copy(
            playerState = STATE_PREPARED,
            currentTime = 0L,
            playButtonVisible = true,
            pauseButtonVisible = false
        )
    }

    fun playbackControl() {
        when (_audioPlayerState.value?.playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun startTimer() {
        viewModelScope.launch {
            while (_audioPlayerState.value?.playerState == STATE_PLAYING) {
                _audioPlayerState.value = _audioPlayerState.value?.copy(
                    currentTime = audioPlayerInteractor.getCurrentPosition().toLong()
                )
                delay(300)
            }
        }
    }
}