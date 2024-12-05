package com.example.playlist_maker.domain.api

interface SettingsRepository {
    var darkTheme: Boolean
    fun switchTheme(isDarkTheme: Boolean)
    fun isDarkThemeSaved(): Boolean
}