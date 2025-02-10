package com.example.playlist_maker.data.search.impl

import com.example.playlist_maker.data.search.NetworkClient
import com.example.playlist_maker.data.search.dto.TrackRequest
import com.example.playlist_maker.domain.search.TrackRepository
import com.example.playlist_maker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {

    override suspend fun search(query: String): Flow<List<Track>?> {
        return networkClient.doRequest(TrackRequest(query))
            .map { response ->
                if (response.resultCode == 200) {
                    val tracks = response.results.map {
                        Track(
                            it.trackId,
                            it.trackName,
                            it.artistName,
                            it.trackTimeMillis,
                            it.artworkUrl100,
                            it.collectionName,
                            it.releaseDate,
                            it.primaryGenreName,
                            it.country,
                            it.previewUrl
                        )
                    }
                    tracks
                } else {
                    null
                }
            }
    }
}