package com.example.playlist_maker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlist_maker.data.db.entity.TrackPlaylistEntity

@Dao
interface TrackPlaylistDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrackPlaylist(trackPlaylist: TrackPlaylistEntity)

    @Query("SELECT * FROM track_playlist_table ORDER BY addedAt DESC")
    suspend fun getAllTracks(): List<TrackPlaylistEntity>

    @Delete
    suspend fun deleteTrackPlaylist(trackPlaylist: TrackPlaylistEntity)
}