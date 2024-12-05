package com.example.playlist_maker.domain.api

import com.example.playlist_maker.domain.models.Track

interface SearchHistoryRepository {
    fun addTrack(track: Track)
    fun getHistory(): List<Track>
    fun clearHistory()
}