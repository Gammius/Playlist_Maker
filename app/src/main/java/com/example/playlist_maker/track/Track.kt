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
                lowerCaseQuery.all { char ->
                    track.trackName.lowercase().contains(char) || track.artistName.lowercase().contains(char)
                }
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