package com.example.playlist_maker.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val namePlaylist: String,
    val descriptionPlaylist: String,
    val uriImageCoverPlaylist: String?,
    var trackIds: String = "[]",
    var trackCount: Int = 0
)