package com.example.playlist_maker.presentation.search.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist_maker.domain.search.TrackInteractor
import com.example.playlist_maker.domain.search.model.Track
import com.example.playlist_maker.domain.searchHistory.SearchHistoryInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val trackInteractor: TrackInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor
) : ViewModel() {

    private val _screenState = MutableStateFlow(SearchScreenState())
    val screenState: StateFlow<SearchScreenState> get() = _screenState

    init {
        loadSearchHistory()
    }

    fun search(query: String) {
        if (query.isEmpty()) {
            _screenState.value = _screenState.value.copy(
                trackList = emptyList(),
                noResults = true
            )
            return
        }
        _screenState.value = _screenState.value.copy(
            isLoading = true,
            trackList = emptyList(),
            noResults = false,
            noInternet = false
        )

        viewModelScope.launch {
            trackInteractor.search(query).collect { trackList ->
                _screenState.value = _screenState.value.copy(isLoading = false)

                if (trackList == null) {
                    _screenState.value = _screenState.value.copy(
                        noInternet = true,
                        trackList = emptyList()
                    )
                } else  {
                    _screenState.value = _screenState.value.copy(
                        trackList = if (trackList.isNotEmpty()) trackList else emptyList(),
                        noResults = trackList.isEmpty()
                    )
                }
                _screenState.value = _screenState.value.copy(lastFailedQuery = query)
            }
        }
    }

    fun addTrackToHistory(track: Track) {
        searchHistoryInteractor.addTrack(track)
        loadSearchHistory()
    }

    fun clearHistory() {
        searchHistoryInteractor.clearHistory()
        loadSearchHistory()
        _screenState.value = _screenState.value.copy(
            trackList = emptyList()
        )
    }

    private fun loadSearchHistory() {
        _screenState.value = _screenState.value.copy(
            historyList = searchHistoryInteractor.getHistory(),
            searchHistoryVisible = true,
        )
    }

    fun clearSearch() {
        _screenState.value = _screenState.value.copy(
            searchText = "",
        )
    }

    fun onSearchEditTextFocusChanged(hasFocus: Boolean) {
        _screenState.value = _screenState.value.copy(isSearchFocused = hasFocus)
    }

    fun retryLastSearch() {
        _screenState.value?.lastFailedQuery?.let { query ->
            search(query)
        }
    }

    fun updateSearchText(newSearchText: String) {
        _screenState.value = _screenState.value.copy(searchText = newSearchText)
    }
}