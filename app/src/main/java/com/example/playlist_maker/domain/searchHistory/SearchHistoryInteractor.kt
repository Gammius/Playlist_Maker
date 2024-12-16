package com.example.playlist_maker.domain.searchHistory

import com.example.playlist_maker.domain.search.model.Track

interface SearchHistoryInteractor {
    fun addTrack(track: Track)
    fun getHistory(): List<Track>
    fun clearHistory()
}