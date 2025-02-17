package com.example.playlist_maker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlist_maker.data.db.dao.TrackDao
import com.example.playlist_maker.data.db.entity.TrackEntity

@Database(entities = [TrackEntity::class], version = 2)
abstract class TrackDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
}