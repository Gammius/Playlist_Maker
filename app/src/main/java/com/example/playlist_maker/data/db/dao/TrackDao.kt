package com.example.playlist_maker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlist_maker.data.db.entity.TrackEntity

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewTrack(trackEntity: TrackEntity)

    @Delete
    suspend fun deleteTrackEntity(trackEntity: TrackEntity)

    @Query("SELECT * FROM track_table ORDER BY addedAt DESC")
    suspend fun getTrackFavorite(): List<TrackEntity>

    @Query("SELECT trackId FROM track_table")
    suspend fun getTrackFavoriteIds(): List<Long>
}