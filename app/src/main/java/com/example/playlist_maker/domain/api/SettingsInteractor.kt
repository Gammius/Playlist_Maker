package com.example.playlist_maker.domain.api

interface SettingsInteractor {
    var darkTheme: Boolean
    fun switchTheme(isDarkTheme: Boolean)
}