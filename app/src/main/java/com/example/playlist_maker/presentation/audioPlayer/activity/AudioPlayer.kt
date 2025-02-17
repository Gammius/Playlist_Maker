package com.example.playlist_maker.presentation.audioPlayer.activity

import android.content.res.Configuration
import com.example.playlist_maker.presentation.audioPlayer.view_model.AudioPlayerViewModel
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlist_maker.R
import com.example.playlist_maker.Utils.dpToPx
import com.example.playlist_maker.domain.search.model.Track
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class AudioPlayer : AppCompatActivity() {

    private val audioPlayerViewModel: AudioPlayerViewModel by viewModel()

    private lateinit var play: ImageButton
    private lateinit var pause: ImageButton
    private lateinit var trackTimeDemo: TextView
    private lateinit var isFavoriteButton: ImageButton

    private var trackId: Long? = null
    private var trackName: String? = null
    private var artistName: String? = null
    private var trackTimeMillis: Long? = null
    private var collectionName: String? = null
    private var releaseDate: String? = null
    private var primaryGenreName: String? = null
    private var country: String? = null
    private var previewUrl: String? = null
    private var coverArtwork: String? = null
    private var isFavorite: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_audio_player)

        intent?.let {
            trackId = it.getLongExtra("track_id", 0L)
            trackName = it.getStringExtra("track_name")
            artistName = it.getStringExtra("artist_name")
            trackTimeMillis = it.getLongExtra("track_time", 0L)
            collectionName = it.getStringExtra("collection_name")
            releaseDate = it.getStringExtra("release_date")
            primaryGenreName = it.getStringExtra("primary_genre_name")
            country = it.getStringExtra("country")
            previewUrl = it.getStringExtra("preview_url")
            coverArtwork = it.getStringExtra("cover_artwork")
            isFavorite = it.getBooleanExtra("is_favorite", false)
        }

        val track = Track(
            trackId = trackId ?: 0L,
            trackName = trackName ?: "",
            artistName = artistName ?: "",
            trackTimeMillis = trackTimeMillis ?: 0L,
            artworkUrl100 = coverArtwork ?: "",
            collectionName = collectionName ?: "",
            releaseDate = releaseDate ?: "",
            primaryGenreName = primaryGenreName ?: "",
            country = country ?: "",
            previewUrl = previewUrl ?: "",
            isFavorite = isFavorite ?: false
        )

        val trackNameTextView: TextView = findViewById(R.id.track_name_ap)
        val artistNameTextView: TextView = findViewById(R.id.artist_name_ap)
        val trackTimeTextView: TextView = findViewById(R.id.track_time_ap)
        val artworkImageView: ImageView = findViewById(R.id.cover_track)
        val collectionNameView: TextView = findViewById(R.id.album_ap)
        val releaseDateView: TextView = findViewById(R.id.year_ap)
        val primaryGenreNameView: TextView = findViewById(R.id.genre_ap)
        val countryView: TextView = findViewById(R.id.country_ap)

        trackNameTextView.text = trackName ?: ""
        artistNameTextView.text = artistName ?: ""
        trackTimeTextView.text = formatTime(trackTimeMillis ?: 0L)
        collectionNameView.text = collectionName ?: ""
        releaseDateView.text = releaseDate ?: ""
        primaryGenreNameView.text = primaryGenreName ?: ""
        countryView.text = country ?: ""
        artistNameTextView.requestLayout()
        val cornerRadiusPx = dpToPx(8f, this)
        Glide.with(this)
            .load(coverArtwork)
            .placeholder(R.drawable.place_holder_ap)
            .centerCrop()
            .transform(RoundedCorners(cornerRadiusPx))
            .into(artworkImageView)

        val buttonBack = findViewById<Button>(R.id.arrow_back)
        buttonBack.setOnClickListener {
            onBackPressed()
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                audioPlayerViewModel.audioPlayerState.collect { state ->
                    play.isVisible = state.playButtonVisible
                    pause.isVisible = state.pauseButtonVisible
                    trackTimeDemo.text = formatTime(state.currentTime)
                }
            }
        }

        play = findViewById(R.id.play_track_btn)
        pause = findViewById(R.id.pause_track_btn)
        trackTimeDemo = findViewById(R.id.track_time_demo)
        isFavoriteButton = findViewById(R.id.like_track_btn)

        audioPlayerViewModel.preparePlayer(previewUrl ?: "")

        play.setOnClickListener {
            audioPlayerViewModel.playbackControl()
        }

        pause.setOnClickListener {
            audioPlayerViewModel.playbackControl()
        }

        audioPlayerViewModel.checkIfTrackIsFavorite(trackId ?: 0L)

        val isDarkTheme = isDarkTheme()
        audioPlayerViewModel.isFavorite.observe(this) { isFavorite ->
            val iconRes = if (isDarkTheme) {
                if (isFavorite) R.drawable.like_on_night else R.drawable.like_night
            } else {
                if (isFavorite) R.drawable.like_on else R.drawable.like
            }
            isFavoriteButton.setImageResource(iconRes)
        }

        isFavoriteButton.setOnClickListener {
            audioPlayerViewModel.onFavoriteClicked(track)
        }
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
        audioPlayerViewModel.resetPlayer()
        super.onDestroy()
    }

    private fun isDarkTheme(): Boolean {
        return (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
    }
}