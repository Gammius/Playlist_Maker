package com.example.playlist_maker.presentation.search.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlist_maker.domain.search.TrackInteractor
import com.example.playlist_maker.domain.searchHistory.SearchHistoryInteractor

class SearchViewModelFactory(
    private val trackInteractor: TrackInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(trackInteractor, searchHistoryInteractor) as T
    }
}