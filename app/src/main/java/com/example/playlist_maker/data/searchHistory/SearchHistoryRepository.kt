package com.example.playlist_maker.data.searchHistory

import com.example.playlist_maker.domain.search.model.Track

interface SearchHistoryRepository {
    fun addTrack(track: Track)
    fun getHistory(): List<Track>
    fun clearHistory()
}