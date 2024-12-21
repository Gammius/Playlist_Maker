package com.example.playlist_maker.presentation.mediaLibrary.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlist_maker.presentation.mediaLibrary.view_model.model.FavoritesFragmentState

class FavoritesViewModel : ViewModel() {

    private val _favorites = MutableLiveData<FavoritesFragmentState>()
    val favorites: LiveData<FavoritesFragmentState> get() = _favorites
}