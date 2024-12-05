package com.example.playlist_maker.domain.models

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Track(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: Date,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String
) {
    fun getFormattedTrackTime(milliseconds: Long): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(milliseconds)
    }

    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

    fun getFormattedReleaseYear(): String {
        return SimpleDateFormat("yyyy", Locale.getDefault()).format(releaseDate)
    }
}