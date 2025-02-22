package com.example.playlist_maker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.playlist_maker.data.db.entity.PlaylistEntity

@Dao
interface PlaylistDao {
    @Insert
    suspend fun insertPlaylist(playlistEntity: PlaylistEntity)

    @Update
    suspend fun updatePlaylist(playlistEntity: PlaylistEntity)

    @Query("SELECT * FROM playlist_table WHERE id = :playlistId LIMIT 1")
    suspend fun getPlaylistById(playlistId: Long): PlaylistEntity?

    @Query("SELECT * FROM playlist_table")
    suspend fun getAllPlaylists(): List<PlaylistEntity>

    @Query("UPDATE playlist_table SET trackIds = :trackIds, trackCount = :trackCount WHERE id = :playlistId")
    suspend fun updatePlayList(playlistId: Long, trackIds: String, trackCount: Int)
}