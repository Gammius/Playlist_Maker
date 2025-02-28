package com.example.playlist_maker.presentation.mediaLibrary.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlist_maker.R
import com.example.playlist_maker.domain.playlist.model.Playlist

class PlaylistsAdapter(
    private var playlists: List<Playlist>,
    private val onPlaylistClick: (Long) -> Unit
) : RecyclerView.Adapter<PlaylistsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_playlist, parent, false)
        return PlaylistsViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistsViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.bind(playlist)

        holder.itemView.setOnClickListener {
            onPlaylistClick(playlist.id)
        }
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    fun updatePlaylist(newPlaylist: List<Playlist>) {
        playlists = newPlaylist
        notifyDataSetChanged()
    }
}