package com.example.playlist_maker.presentation.mediaLibrary.activity

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.playlist_maker.R
import com.example.playlist_maker.Utils.dpToPx
import com.example.playlist_maker.Utils.getTrackCountText
import com.example.playlist_maker.domain.playlist.model.Playlist

class PlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val namePlaylistView: TextView = itemView.findViewById(R.id.name_playlist)
    private val countTrackView: TextView = itemView.findViewById(R.id.count_track)
    private val imageCoverView: ImageView = itemView.findViewById(R.id.image_cover)

    fun bind(playlist: Playlist) {
        namePlaylistView.text = playlist.namePlaylist
        countTrackView.text = getTrackCountText(playlist.trackCount)
        val context = itemView.context
        val cornerRadiusPx = dpToPx(8f, context)
        Glide.with(itemView)
            .load(playlist.uriImageCoverPlaylist)
            .placeholder(R.drawable.place_holder_pl)
            .apply(
                RequestOptions()
                    .transform(
                        MultiTransformation(
                            CenterCrop(),
                            RoundedCorners(cornerRadiusPx)
                        )
                    )
            )
            .into(imageCoverView)
    }
}