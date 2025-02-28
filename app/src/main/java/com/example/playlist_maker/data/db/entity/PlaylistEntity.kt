package com.example.playlist_maker.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var namePlaylist: String,
    var descriptionPlaylist: String,
    var uriImageCoverPlaylist: String?,
    var trackIds: String = "[]",
    var trackCount: Int = 0
)