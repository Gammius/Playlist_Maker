package com.example.playlist_maker.data.settings

import com.example.playlist_maker.domain.settings.model.ThemeSettings

interface SettingsRepository {
    var darkTheme: Boolean
    fun switchTheme(isDarkTheme: Boolean) : ThemeSettings
    fun isDarkThemeSaved(): Boolean
}