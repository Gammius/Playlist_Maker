package com.example.playlist_maker.data.playlist.impl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Log
import com.example.playlist_maker.data.converters.PlaylistDbConvertor
import com.example.playlist_maker.data.converters.TrackPlaylistDbConvertor
import com.example.playlist_maker.data.db.dao.PlaylistDao
import com.example.playlist_maker.data.db.dao.TrackPlaylistDao
import com.example.playlist_maker.data.db.entity.PlaylistEntity
import com.example.playlist_maker.data.db.entity.TrackPlaylistEntity
import com.example.playlist_maker.domain.playlist.PlaylistRepository
import com.example.playlist_maker.domain.playlist.model.Playlist
import com.example.playlist_maker.domain.search.model.Track
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.FileOutputStream

class PlaylistRepositoryImpl(
    private val context: Context,
    private val playlistDao: PlaylistDao,
    private val trackPlaylistDao: TrackPlaylistDao,
    private val playlistDbConverter: PlaylistDbConvertor,
    private val trackPlaylistDbConvertor: TrackPlaylistDbConvertor
) : PlaylistRepository {

    override suspend fun addPlaylist(playlist: Playlist) {
        val savedImageUri = saveImageToAppStorage(playlist.uriImageCoverPlaylist)
        val playlistWithSavedImage = playlist.copy(uriImageCoverPlaylist = savedImageUri)
        val playlistDb = convertToPlaylistEntity(playlistWithSavedImage)
        playlistDao.insertPlaylist(playlistDb)
    }

    override suspend fun getAllPlaylist(): Flow<List<Playlist>> = flow {
        val playlists = playlistDao.getAllPlaylists()
        emit(convertFromPlaylistEntity(playlists))
    }

    override suspend fun updatePlayList(playlistId: Long, trackIds: List<Long>, trackCount: Int) {
        val playlistDb = playlistDao.getPlaylistById(playlistId)
        if (playlistDb != null) {
            playlistDb.trackIds = Gson().toJson(trackIds)
            playlistDb.trackCount = trackCount

            playlistDao.updatePlaylist(playlistDb)
        }
    }

    override suspend fun addTrackPlaylist(track: Track) {
        val trackDb = convertToTrackPlaylist(track)
        trackPlaylistDao.insertTrackPlaylist(trackDb)
    }

    override suspend fun getPlaylistById(playlistId: Long): Flow<Playlist?> = flow {
        val playlist = playlistDao.getPlaylistById(playlistId)
        emit(playlist?.let { playlistDbConverter.map(it) })
    }

    override suspend fun getAllTracks(): Flow<List<Track>> = flow {
        val tracks = trackPlaylistDao.getAllTracks()
        emit(convertFromTrackPlaylist(tracks))
    }

    override suspend fun deleteTrackPlaylist(track: Track) {
        val trackDb = convertToTrackPlaylist(track)
        trackPlaylistDao.deleteTrackPlaylist(trackDb)
    }

    override suspend fun deletePlaylistById(playlistId: Long) {
        playlistDao.deletePlaylistById(playlistId)
    }

    override suspend fun updateEditPlaylist(
        playlistId: Long,
        namePlaylist: String,
        descriptionPlaylist: String,
        uriImageCoverPlaylist: Uri?
    ) {
        val savedImageUri: Uri? = if (uriImageCoverPlaylist.toString() != "null"){
            saveImageToAppStorage(uriImageCoverPlaylist)
        } else {
            null
        }

            val playlistDb = playlistDao.getPlaylistById(playlistId)
            if (playlistDb != null) {
                playlistDb.namePlaylist = namePlaylist
                playlistDb.descriptionPlaylist = descriptionPlaylist
                playlistDb.uriImageCoverPlaylist = savedImageUri?.toString()
                playlistDao.updatePlaylist(playlistDb)
                Log.d("SaveData3", "Saving playlist with image URI: $playlistDb")
            }
    }


    override suspend fun deleteTrackById(trackId: Long) {
        trackPlaylistDao.deleteTrackById(trackId)
    }

    private fun convertToPlaylistEntity(playlist: Playlist): PlaylistEntity {
        return playlistDbConverter.map(playlist)
    }

    private fun convertFromPlaylistEntity(playlistsDb: List<PlaylistEntity>): List<Playlist> {
        return playlistsDb.map { playlist -> playlistDbConverter.map(playlist) }
    }

    private fun convertFromTrackPlaylist(trackDb: List<TrackPlaylistEntity>): List<Track> {
        return trackDb.map { track -> trackPlaylistDbConvertor.map(track) }
    }

    private fun convertToTrackPlaylist(track: Track): TrackPlaylistEntity {
        val addedAt = System.currentTimeMillis()
        return trackPlaylistDbConvertor.map(track, addedAt)
    }

    private fun saveImageToAppStorage(uri: Uri?): Uri? {
        if (uri.toString() != "null") {
            val filePath =
                File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlist_images")

            if (!filePath.exists()) {
                filePath.mkdirs()
            }

            val fileName = "cover_${System.currentTimeMillis()}.jpg"
            val file = File(filePath, fileName)
            val inputStream = uri?.let { context.contentResolver.openInputStream(it) }
            val outputStream = FileOutputStream(file)

            inputStream?.use { input ->
                outputStream.use { output ->
                    BitmapFactory.decodeStream(input)
                        .compress(Bitmap.CompressFormat.JPEG, 80, output)
                }
            }
            return  Uri.fromFile(file)
        } else {
            return  null
        }
    }
}