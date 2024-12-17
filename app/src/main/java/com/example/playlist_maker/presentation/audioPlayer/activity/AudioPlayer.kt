package com.example.playlist_maker.presentation.audioPlayer.activity

import com.example.playlist_maker.presentation.audioPlayer.view_model.AudioPlayerViewModel
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlist_maker.R
import com.example.playlist_maker.Utils.dpToPx
import com.example.playlist_maker.presentation.search.activity.SearchActivity.TrackHolder
import org.koin.androidx.viewmodel.ext.android.viewModel


class AudioPlayer : AppCompatActivity() {

    private val audioPlayerViewModel: AudioPlayerViewModel by viewModel()

    private lateinit var play: ImageButton
    private lateinit var pause: ImageButton
    private lateinit var trackTimeDemo: TextView

    private val track = TrackHolder.selectedTrack

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_audio_player)

        val buttonBack = findViewById<Button>(R.id.arrow_back)
        buttonBack.setOnClickListener {
            onBackPressed()
        }

        audioPlayerViewModel.audioPlayerState.observe(this) { state ->
            play.isVisible = state.playButtonVisible
            pause.isVisible = state.pauseButtonVisible
            trackTimeDemo.text = formatTime(state.currentTime)
        }

        play = findViewById(R.id.play_track_btn)
        pause = findViewById(R.id.pause_track_btn)
        trackTimeDemo = findViewById(R.id.track_time_demo)


        audioPlayerViewModel.preparePlayer(track?.previewUrl ?: "")

        play.setOnClickListener {
            audioPlayerViewModel.playbackControl()
        }

        pause.setOnClickListener {
            audioPlayerViewModel.playbackControl()
        }

        val trackNameTextView: TextView = findViewById(R.id.track_name_ap)
        val artistNameTextView: TextView = findViewById(R.id.artist_name_ap)
        val trackTimeTextView: TextView = findViewById(R.id.track_time_ap)
        val artworkImageView: ImageView = findViewById(R.id.cover_track)
        val collectionNameView: TextView = findViewById(R.id.album_ap)
        val releaseDateView: TextView = findViewById(R.id.year_ap)
        val primaryGenreNameView: TextView = findViewById(R.id.genre_ap)
        val countryView: TextView = findViewById(R.id.country_ap)

        trackNameTextView.text = track?.trackName ?: ""
        artistNameTextView.text = track?.artistName ?: ""
        trackTimeTextView.text = track?.getFormattedTrackTime(track.trackTimeMillis) ?: ""
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

    private fun formatTime(time: Long): String {
        val minutes = (time / 1000) / 60
        val seconds = (time / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
    override fun onPause() {
        super.onPause()
        audioPlayerViewModel.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        audioPlayerViewModel.resetPlayer()
    }
}