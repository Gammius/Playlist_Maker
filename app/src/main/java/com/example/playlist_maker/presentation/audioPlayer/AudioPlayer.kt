package com.example.playlist_maker.presentation.audioPlayer

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlist_maker.R
import com.example.playlist_maker.presentation.search.SearchActivity.TrackHolder
import com.example.playlist_maker.Utils.dpToPx

class AudioPlayer : AppCompatActivity() {

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }

    private val track = TrackHolder.selectedTrack
    private var playerState = STATE_DEFAULT
    private lateinit var play: ImageButton
    private lateinit var pause: ImageButton
    private lateinit var trackTimeDemo: TextView
    private var mediaPlayer = MediaPlayer()
    private val handler = Handler(Looper.getMainLooper())
    private val updateTimeRunnable = object : Runnable {
        override fun run() {
            if (playerState == STATE_PLAYING) {
                val currentTime = mediaPlayer.currentPosition.toLong()
                trackTimeDemo.text = track?.getFormattedTrackTime(currentTime)
                handler.postDelayed(this, 300)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        val buttonBack = findViewById<Button>(R.id.arrow_back)
        buttonBack.setOnClickListener {
            onBackPressed()
        }

        trackTimeDemo = findViewById(R.id.track_time_demo)

        play = findViewById(R.id.play_track_btn)
        play.setOnClickListener {
            playbackControl()
        }
        pause = findViewById(R.id.pause_track_btn)
        pause.setOnClickListener {
            playbackControl()
        }

        preparePlayer()

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

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(track?.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            play.isEnabled = true
            pause.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            play.visibility = View.VISIBLE
            pause.visibility = View.GONE
            trackTimeDemo.text = "00:00"
            handler.removeCallbacks(updateTimeRunnable)
            playerState = STATE_PREPARED
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        play.visibility = View.GONE
        pause.visibility = View.VISIBLE
        playerState = STATE_PLAYING
        handler.post(updateTimeRunnable)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        play.visibility = View.VISIBLE
        pause.visibility = View.GONE
        playerState = STATE_PAUSED
        handler.removeCallbacks(updateTimeRunnable)
    }
}