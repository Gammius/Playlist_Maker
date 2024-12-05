package com.example.playlist_maker.data

import com.example.playlist_maker.data.dto.TrackRequest
import com.example.playlist_maker.data.dto.TrackResponse
import com.example.playlist_maker.domain.api.TrackRepository
import com.example.playlist_maker.domain.models.Track

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {
    override fun search(query: String): List<Track> {
        val response = networkClient.doRequest(TrackRequest(query))
        if (response.resultCode == 200) {
            return response.results.map {
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
        } else {
            return emptyList()
        }
    }
}