package com.example.playlist_maker.presentation.search.view_model

import com.example.playlist_maker.domain.search.model.Track

data class SearchScreenState(
    val trackList: List<Track> = emptyList(),
    val historyList: List<Track> = emptyList(),
    val isLoading: Boolean = false,
    val noResults: Boolean = false,
    val noInternet: Boolean = false,
    val searchText: String = "",
    val isSearchFocused: Boolean = false,
    val searchHistoryVisible: Boolean = false,
    val lastFailedQuery: String? = null
)