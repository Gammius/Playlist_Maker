package com.example.playlist_maker

import android.content.Context
import com.example.playlist_maker.data.SearchHistoryRepositoryImpl
import com.example.playlist_maker.data.TrackRepositoryImpl
import com.example.playlist_maker.data.network.RetrofitNetworkClient
import com.example.playlist_maker.domain.api.SearchHistoryInteractor
import com.example.playlist_maker.domain.api.SearchHistoryRepository
import com.example.playlist_maker.domain.api.TrackInteractor
import com.example.playlist_maker.domain.api.TrackRepository
import com.example.playlist_maker.domain.impl.SearchHistoryInteractorImpl
import com.example.playlist_maker.domain.impl.TrackInteractorImpl

object Creator {
    private fun getTrackRepository(): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }

    private fun getSearchHistoryRepository(context: Context): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(context)
    }

    fun provideTrackInteractor(): TrackInteractor {
        return TrackInteractorImpl(getTrackRepository())
    }

    fun provideSearchHistoryInteractor(context: Context): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(getSearchHistoryRepository(context))
    }
}