package com.example.playlist_maker.presentation.mediaLibrary.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlist_maker.presentation.mediaLibrary.view_model.model.PlaylistFragmentState

class PlaylistViewModel : ViewModel() {

    private val _playlist = MutableLiveData<PlaylistFragmentState>()
    val playlist: LiveData<PlaylistFragmentState> get() = _playlist
}