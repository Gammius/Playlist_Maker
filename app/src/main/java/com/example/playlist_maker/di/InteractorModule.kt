package com.example.playlist_maker.di

import com.example.playlist_maker.domain.audioPlayer.AudioPlayerInteractor
import com.example.playlist_maker.domain.audioPlayer.impl.AudioPlayerInteractorImpl
import com.example.playlist_maker.domain.favoriteTrack.FavoriteTrackInteractor
import com.example.playlist_maker.domain.favoriteTrack.impl.FavoriteTrackInteractorImpl
import com.example.playlist_maker.domain.playlist.PlaylistInteractor
import com.example.playlist_maker.domain.playlist.impl.PlaylistInteractorImpl
import com.example.playlist_maker.domain.search.TrackInteractor
import com.example.playlist_maker.domain.search.impl.TrackInteractorImpl
import com.example.playlist_maker.domain.searchHistory.SearchHistoryInteractor
import com.example.playlist_maker.domain.searchHistory.impl.SearchHistoryInteractorImpl
import com.example.playlist_maker.domain.settings.SettingsInteractor
import com.example.playlist_maker.domain.settings.impl.SettingsInteractorImpl
import com.example.playlist_maker.domain.sharing.SharingInteractor
import com.example.playlist_maker.domain.sharing.impl.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    single<AudioPlayerInteractor> {
        AudioPlayerInteractorImpl(get())
    }

    single<TrackInteractor> {
        TrackInteractorImpl(get())
    }

    single<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(get())
    }

    single<FavoriteTrackInteractor> {
        FavoriteTrackInteractorImpl(get())
    }

    single<PlaylistInteractor> {
        PlaylistInteractorImpl(get())
    }
}