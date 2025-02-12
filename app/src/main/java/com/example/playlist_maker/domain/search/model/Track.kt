package com.example.playlist_maker.domain.search.model

import java.text.SimpleDateFormat
import java.util.Locale

data class Track(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
    var isFavorite: Boolean = false
) {

    fun getFormattedTrackTime(milliseconds: Long): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(milliseconds)
    }

    fun getFormattedReleaseYear(releaseDate: String?): String {
        return if (releaseDate != null && releaseDate.isNotEmpty()) {
            try {
                val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(releaseDate)
                date?.let {
                    SimpleDateFormat("yyyy", Locale.getDefault()).format(it)
                } ?: ""
            } catch (e: Exception) {
                ""
            }
        } else {
            ""
        }
    }

    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
}