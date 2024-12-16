package com.example.playlist_maker.domain.searchHistory.impl

import com.example.playlist_maker.domain.searchHistory.SearchHistoryRepository
import com.example.playlist_maker.domain.search.model.Track
import com.example.playlist_maker.domain.searchHistory.SearchHistoryInteractor

class SearchHistoryInteractorImpl (private val repository: SearchHistoryRepository) :
    SearchHistoryInteractor {
    override fun addTrack(track: Track) {
        repository.addTrack(track)
    }

    override fun getHistory(): List<Track> {
        return repository.getHistory()
    }

    override fun clearHistory() {
        repository.clearHistory()
    }
}