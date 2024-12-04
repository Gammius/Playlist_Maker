package com.example.playlist_maker.domain.impl

import com.example.playlist_maker.domain.api.SearchHistoryInteractor
import com.example.playlist_maker.domain.api.SearchHistoryRepository
import com.example.playlist_maker.domain.models.Track

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