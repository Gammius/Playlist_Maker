package com.example.playlist_maker.di

import com.example.playlist_maker.presentation.audioPlayer.view_model.AudioPlayerViewModel
import com.example.playlist_maker.presentation.search.view_model.SearchViewModel
import com.example.practicum.playlist.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { AudioPlayerViewModel(get()) }
    viewModel { SearchViewModel(get(), get()) }
    viewModel { SettingsViewModel(get(), get()) }
}