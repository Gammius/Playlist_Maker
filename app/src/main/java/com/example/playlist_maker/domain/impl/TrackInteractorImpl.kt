package com.example.playlist_maker.domain.impl

import android.os.Handler
import android.os.Looper
import com.example.playlist_maker.domain.api.TrackInteractor
import com.example.playlist_maker.domain.api.TrackRepository
import java.util.concurrent.Executors

class TrackInteractorImpl(private val repository: TrackRepository) : TrackInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun search(query: String, consumer: TrackInteractor.TrackConsumer) {
        executor.execute {
            val trackList = repository.search(query)
            Handler(Looper.getMainLooper()).post {
                consumer.consume(trackList)
            }
        }
    }
}