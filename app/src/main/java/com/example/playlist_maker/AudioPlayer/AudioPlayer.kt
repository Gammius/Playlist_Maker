package com.example.playlist_maker.AudioPlayer

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlist_maker.R
import com.example.playlist_maker.SearchActivity.TrackHolder
import com.example.playlist_maker.Utils.dpToPx

class AudioPlayer : AppCompatActivity() {

    private val track = TrackHolder.selectedTrack
    private val trackTimeDemo: Long = 30*1000


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        val buttonBack = findViewById<Button>(R.id.arrow_back)
        buttonBack.setOnClickListener {
            onBackPressed()
        }

        val trackNameTextView: TextView = findViewById(R.id.track_name_ap)
        val artistNameTextView: TextView = findViewById(R.id.artist_name_ap)
        val trackTimeTextView: TextView = findViewById(R.id.track_time_ap)
        val demo: TextView = findViewById(R.id.track_time_demo)
        val artworkImageView: ImageView = findViewById(R.id.cover_track)
        val collectionNameView: TextView = findViewById(R.id.album_ap)
        val releaseDateView: TextView = findViewById(R.id.year_ap)
        val primaryGenreNameView: TextView = findViewById(R.id.genre_ap)
        val countryView: TextView = findViewById(R.id.country_ap)

        trackNameTextView.text = track?.trackName ?: ""
        artistNameTextView.text = track?.artistName ?: ""
        trackTimeTextView.text = track?.getFormattedTrackTime(track.trackTimeMillis) ?: ""
        demo.text = track?.getFormattedTrackTime(trackTimeDemo) ?: ""
        collectionNameView.text = track?.collectionName ?: ""
        releaseDateView.text = track?.getFormattedReleaseYear() ?: ""
        primaryGenreNameView.text = track?.primaryGenreName ?: ""
        countryView.text = track?.country ?: ""
        artistNameTextView.requestLayout()
        val cornerRadiusPx = dpToPx(8f, this)
        Glide.with(this)
            .load(track?.getCoverArtwork())
            .placeholder(R.drawable.place_holder_ap)
            .centerCrop()
            .transform(RoundedCorners(cornerRadiusPx))
            .into(artworkImageView)
    }
}