package com.example.playlist_maker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlist_maker.track.Track
import com.example.playlist_maker.track.TrackViewHolder

class SearchHistoryAdapter(
    private var trackList: List<Track>
) : RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = trackList[position]
        holder.bind(track)
    }

    override fun getItemCount(): Int = trackList.size

    fun updateTracks(newTrackList: List<Track>) {
        trackList = newTrackList
        notifyDataSetChanged()
    }
}