package com.example.playlist_maker.presentation.mediaLibrary.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlist_maker.presentation.mediaLibrary.view_model.model.MediaLibraryActivityState

class MediaLibraryViewModel : ViewModel() {

    private val _mediaLibrary = MutableLiveData<MediaLibraryActivityState>()
    val mediaLibrary: LiveData<MediaLibraryActivityState> get() = _mediaLibrary

    init {
        _mediaLibrary.value = MediaLibraryActivityState(
            selectedTabIndex = 0
        )
    }

    fun selectedTabIndex(index: Int) {
        _mediaLibrary.value = _mediaLibrary.value?.copy(selectedTabIndex = index)
    }
}