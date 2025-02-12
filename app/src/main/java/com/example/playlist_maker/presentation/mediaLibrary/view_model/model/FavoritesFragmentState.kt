package com.example.playlist_maker.presentation.mediaLibrary.view_model.model

import com.example.playlist_maker.domain.search.model.Track

data class FavoritesFragmentState(
    val trackList: List<Track> = emptyList(),
    val noMedia: Boolean = true,
)