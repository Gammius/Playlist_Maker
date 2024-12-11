package com.example.playlist_maker.Creator

import android.content.Context
import android.media.MediaPlayer
import com.example.playlist_maker.data.audioPlayer.AudioPlayerRepository
import com.example.playlist_maker.data.audioPlayer.impl.AudioPlayerRepositoryImpl
import com.example.playlist_maker.data.searchHistory.impl.SearchHistoryRepositoryImpl
import com.example.playlist_maker.data.settings.impl.SettingsRepositoryImpl
import com.example.playlist_maker.data.search.impl.TrackRepositoryImpl
import com.example.playlist_maker.data.search.network.RetrofitNetworkClient
import com.example.playlist_maker.domain.searchHistory.SearchHistoryInteractor
import com.example.playlist_maker.data.searchHistory.SearchHistoryRepository
import com.example.playlist_maker.domain.settings.SettingsInteractor
import com.example.playlist_maker.data.settings.SettingsRepository
import com.example.playlist_maker.data.sharing.ExternalNavigator
import com.example.playlist_maker.data.sharing.impl.ExternalNavigatorImpl
import com.example.playlist_maker.domain.search.TrackInteractor
import com.example.playlist_maker.data.search.TrackRepository
import com.example.playlist_maker.domain.audioPlayer.AudioPlayerInteractor
import com.example.playlist_maker.domain.audioPlayer.impl.AudioPlayerInteractorImpl
import com.example.playlist_maker.domain.searchHistory.impl.SearchHistoryInteractorImpl
import com.example.playlist_maker.domain.settings.impl.SettingsInteractorImpl
import com.example.playlist_maker.domain.search.impl.TrackInteractorImpl
import com.example.playlist_maker.domain.sharing.SharingInteractor
import com.example.playlist_maker.domain.sharing.impl.SharingInteractorImpl

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

    private fun getSettingsRepository(context: Context) : SettingsRepository {
        return SettingsRepositoryImpl(context)
    }

    fun providerSettingsInteractor(context: Context) : SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository(context))
    }

    private fun getSharingRepository(context: Context) : ExternalNavigator {
        return ExternalNavigatorImpl(context)
    }

    fun providerSharingInteractor(context: Context) : SharingInteractor {
        return  SharingInteractorImpl(getSharingRepository(context))
    }

    private fun getAudioPlayerRepository() : AudioPlayerRepository {
        return AudioPlayerRepositoryImpl(MediaPlayer())
    }

    fun providerAudioPlayerInteractor() : AudioPlayerInteractor {
        return AudioPlayerInteractorImpl(getAudioPlayerRepository())
    }


}