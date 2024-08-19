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
            return tracks.filter {
                it.trackName.contains(query, ignoreCase = true) ||
                        it.artistName.contains(query, ignoreCase = true)
            }
        }
    }

    fun getFormattedTrackTime(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)
    }
}