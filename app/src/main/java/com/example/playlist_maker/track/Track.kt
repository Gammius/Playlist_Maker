package com.example.playlist_maker.track

import java.text.SimpleDateFormat
import java.util.Locale

data class Track(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String
) {
    companion object {

        fun filterTracks(query: String, tracks: List<Track>): List<Track> {
            val lowerCaseQuery = query.lowercase()

            return tracks.filter { track ->
                track.trackName.lowercase().contains(lowerCaseQuery) ||
                        track.artistName.lowercase().contains(lowerCaseQuery)
            }.sortedBy {
                when {
                    it.trackName.lowercase().startsWith(lowerCaseQuery) -> 0
                    it.artistName.lowercase().startsWith(lowerCaseQuery) -> 1
                    else -> 2
                }
            }
        }
    }

    fun getFormattedTrackTime(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)
    }
}