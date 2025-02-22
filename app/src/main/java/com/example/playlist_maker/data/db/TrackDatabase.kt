package com.example.playlist_maker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlist_maker.data.db.dao.PlaylistDao
import com.example.playlist_maker.data.db.dao.TrackDao
import com.example.playlist_maker.data.db.dao.TrackPlaylistDao
import com.example.playlist_maker.data.db.entity.PlaylistEntity
import com.example.playlist_maker.data.db.entity.TrackEntity
import com.example.playlist_maker.data.db.entity.TrackPlaylistEntity

@Database(entities = [PlaylistEntity::class, TrackEntity::class,TrackPlaylistEntity::class], version = 2)
abstract class TrackDatabase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao
    abstract fun trackDao(): TrackDao
    abstract fun trackPlaylistDao(): TrackPlaylistDao
}