package com.example.playlist_maker.track

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlist_maker.R
import com.example.playlist_maker.Utils.dpToPx

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackNameTextView: TextView = itemView.findViewById(R.id.track_name)
    private val artistNameTextView: TextView = itemView.findViewById(R.id.artist_name)
    private val trackTimeTextView: TextView = itemView.findViewById(R.id.track_time)
    private val artworkImageView: ImageView = itemView.findViewById(R.id.iv_artwork)

    fun bind(track: Track) {
        trackNameTextView.text = track.trackName
        artistNameTextView.text = track.artistName
        trackTimeTextView.text = track.getFormattedTrackTime(track.trackTimeMillis)
        artistNameTextView.requestLayout()
        val context = itemView.context
        val cornerRadiusPx = dpToPx(2f, context)
        Glide.with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.place_holder)
            .centerCrop()
            .transform(RoundedCorners(cornerRadiusPx))
            .into(artworkImageView)
    }
}