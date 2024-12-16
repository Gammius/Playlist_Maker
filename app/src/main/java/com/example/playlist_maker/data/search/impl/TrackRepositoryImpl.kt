package com.example.playlist_maker.data.search.impl

import com.example.playlist_maker.data.search.NetworkClient
import com.example.playlist_maker.data.search.dto.TrackRequest
import com.example.playlist_maker.domain.search.TrackRepository
import com.example.playlist_maker.domain.search.model.Track

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {

    override fun search(query: String, callback: (List<Track>?) -> Unit) {
        networkClient.doRequest(TrackRequest(query)) { response ->
            if (response.resultCode == 200) {
                val tracks = response.results.map {
                    Track(
                        it.trackId,
                        it.trackName,
                        it.trackName,
                        it.trackTimeMillis,
                        it.artworkUrl100,
                        it.collectionName,
                        it.releaseDate,
                        it.primaryGenreName,
                        it.country,
                        it.previewUrl
                    )
                }
                callback(tracks)
            } else {
                callback(null)
            }
        }
    }
}