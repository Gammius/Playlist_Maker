package com.example.playlist_maker.presentation.audioPlayer.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist_maker.domain.audioPlayer.AudioPlayerInteractor
import com.example.playlist_maker.domain.audioPlayer.model.AudioPlayerEvent
import com.example.playlist_maker.domain.favoriteTrack.FavoriteTrackInteractor
import com.example.playlist_maker.domain.search.model.Track
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AudioPlayerViewModel(
    private val audioPlayerInteractor: AudioPlayerInteractor,
    private val favoriteTrackInteractor: FavoriteTrackInteractor
) : ViewModel() {

    companion object {
        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
    }

    private val _audioPlayerState = MutableStateFlow(AudioPlayerState())
    val audioPlayerState: StateFlow<AudioPlayerState> get() = _audioPlayerState

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> get() = _isFavorite

    init {
        _audioPlayerState.value = AudioPlayerState()
    }

    fun onFavoriteClicked(track: Track) {
        viewModelScope.launch {
            if (track.isFavorite) {
                favoriteTrackInteractor.removeTrackFromFavorite(track)
            } else {
                favoriteTrackInteractor.addTrackToFavorite(track)
            }
            track.isFavorite = !track.isFavorite
            _isFavorite.postValue(track.isFavorite)
        }
    }

    fun checkIfTrackIsFavorite(trackId: Long) {
        viewModelScope.launch {
            val isFavorite = favoriteTrackInteractor.getTrackFavoriteIds(trackId)
            _isFavorite.postValue(isFavorite)
        }
    }

    fun preparePlayer(previewUrl: String) {
        viewModelScope.launch {
            audioPlayerInteractor.preparePlayer(previewUrl).collect { event ->
                when (event) {
                    is AudioPlayerEvent.Prepared -> {
                        _audioPlayerState.value = _audioPlayerState.value.copy(
                            playerState = STATE_PREPARED,
                            playButtonVisible = true,
                            pauseButtonVisible = false
                        )
                    }

                    is AudioPlayerEvent.Completed -> {
                        _audioPlayerState.value = _audioPlayerState.value.copy(
                            playerState = STATE_PREPARED,
                            currentTime = 0L,
                            playButtonVisible = true,
                            pauseButtonVisible = false
                        )
                    }

                    else -> {}
                }
            }
        }
    }

    fun startPlayer() {
        viewModelScope.launch {
            audioPlayerInteractor.startPlayer().collect { event ->
                when (event) {
                    is AudioPlayerEvent.Started -> {
                        _audioPlayerState.value = _audioPlayerState.value.copy(
                            playerState = STATE_PLAYING,
                            playButtonVisible = false,
                            pauseButtonVisible = true
                        )
                        startTimer()
                    }

                    else -> {}
                }
            }
        }
    }

    fun pausePlayer() {
        viewModelScope.launch {
            audioPlayerInteractor.pausePlayer().collect { event ->
                when (event) {
                    is AudioPlayerEvent.Paused -> {
                        _audioPlayerState.value = _audioPlayerState.value.copy(
                            playerState = STATE_PAUSED,
                            playButtonVisible = true,
                            pauseButtonVisible = false
                        )
                    }

                    else -> {}
                }
            }
        }
    }

    fun resetPlayer() {
        viewModelScope.launch {
            audioPlayerInteractor.resetPlayer().collect { event ->
                when (event) {
                    is AudioPlayerEvent.Reset -> {
                        _audioPlayerState.value = _audioPlayerState.value.copy(
                            playerState = STATE_DEFAULT,
                            currentTime = 0L,
                            playButtonVisible = false,
                            pauseButtonVisible = false
                        )
                    }

                    else -> {}
                }
            }
        }
    }

    fun playbackControl() {
        when (_audioPlayerState.value.playerState) {
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
            while (_audioPlayerState.value.playerState == STATE_PLAYING) {
                _audioPlayerState.value = _audioPlayerState.value.copy(
                    currentTime = audioPlayerInteractor.getCurrentPosition().toLong()
                )
                delay(300)
            }
        }
    }
}
