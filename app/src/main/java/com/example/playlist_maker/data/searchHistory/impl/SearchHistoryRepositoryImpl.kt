package com.example.playlist_maker.data.searchHistory.impl

import android.content.SharedPreferences
import com.example.playlist_maker.domain.searchHistory.SearchHistoryRepository
import com.example.playlist_maker.domain.search.model.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistoryRepositoryImpl(private val sharedPreferences: SharedPreferences,private val gson: Gson) : SearchHistoryRepository {
    private val maxHistorySize = 10

    override fun addTrack(track: Track) {
        val history = getHistory().toMutableList()
        history.removeIf { it.trackId == track.trackId }
        history.add(0, track)
        if (history.size > maxHistorySize) {
            history.removeAt(history.size - 1)
        }
        saveHistory(history)
    }

    override fun getHistory(): List<Track> {
        val json = sharedPreferences.getString("history", "[]")
        val type = object : TypeToken<List<Track>>() {}.type
        return gson.fromJson(json, type)
    }

    override fun clearHistory() {
        sharedPreferences.edit().remove("history").apply()
    }

    private fun saveHistory(history: List<Track>) {
        val json = gson.toJson(history)
        sharedPreferences.edit().putString("history", json).apply()
    }
}