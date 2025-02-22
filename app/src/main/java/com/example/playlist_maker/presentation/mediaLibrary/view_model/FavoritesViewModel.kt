package com.example.playlist_maker.presentation.mediaLibrary.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist_maker.domain.favoriteTrack.FavoriteTrackInteractor
import com.example.playlist_maker.presentation.mediaLibrary.view_model.model.FavoritesFragmentState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val favoriteTrackInteractor: FavoriteTrackInteractor
) : ViewModel() {

    private val _favorites = MutableStateFlow(FavoritesFragmentState())
    val favorites: StateFlow<FavoritesFragmentState> get() = _favorites

    init {
        loadFavoriteTracks()
    }

    fun loadFavoriteTracks() {
        viewModelScope.launch {
            favoriteTrackInteractor.getTrackFavorite().collect { tracks ->
                val newState = if (tracks.isEmpty()) {
                    FavoritesFragmentState(noMedia = true)
                } else {
                    FavoritesFragmentState(trackList = tracks, noMedia = false)
                }
                _favorites.value = newState
            }
        }
    }
}