package com.example.playlist_maker.di

import android.media.MediaPlayer
import com.example.playlist_maker.data.audioPlayer.impl.AudioPlayerRepositoryImpl
import com.example.playlist_maker.data.converters.TrackDbConvertor
import com.example.playlist_maker.data.db.TrackDatabase
import com.example.playlist_maker.data.db.dao.TrackDao
import com.example.playlist_maker.data.favoriteTrack.impl.FavoriteTrackRepositoryImpl
import com.example.playlist_maker.data.search.impl.TrackRepositoryImpl
import com.example.playlist_maker.data.searchHistory.impl.SearchHistoryRepositoryImpl
import com.example.playlist_maker.data.settings.impl.APP_PREFERENCES
import com.example.playlist_maker.data.settings.impl.SettingsRepositoryImpl
import com.example.playlist_maker.data.sharing.impl.ExternalNavigatorImpl
import com.example.playlist_maker.domain.audioPlayer.AudioPlayerRepository
import com.example.playlist_maker.domain.favoriteTrack.FavoriteTrackRepository
import com.example.playlist_maker.domain.search.TrackRepository
import com.example.playlist_maker.domain.searchHistory.SearchHistoryRepository
import com.example.playlist_maker.domain.settings.SettingsRepository
import com.example.playlist_maker.domain.sharing.ExternalNavigator
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val repositoryModule = module {

    single {
        MediaPlayer()
    }

    single<AudioPlayerRepository> {
        AudioPlayerRepositoryImpl(get())
    }

    single<TrackRepository> {
        TrackRepositoryImpl(get())
    }

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get { parametersOf("search_history") }, get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get { parametersOf(APP_PREFERENCES) })
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(get())
    }

    single<FavoriteTrackRepository> {
        FavoriteTrackRepositoryImpl(get(), get())
    }

    single<TrackDao> {
        get<TrackDatabase>().trackDao()
    }

    single {
        TrackDbConvertor()
    }
}