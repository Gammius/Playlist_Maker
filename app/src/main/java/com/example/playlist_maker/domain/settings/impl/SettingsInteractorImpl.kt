package com.example.playlist_maker.domain.settings.impl

import com.example.playlist_maker.domain.settings.SettingsInteractor
import com.example.playlist_maker.domain.settings.SettingsRepository
import com.example.playlist_maker.domain.settings.model.ThemeSettings

class SettingsInteractorImpl (private val repository: SettingsRepository) : SettingsInteractor {
    override var darkTheme: Boolean
        get() = repository.darkTheme
        set(value) {
            repository.switchTheme(value)
        }

    override fun switchTheme(isDarkTheme: Boolean): ThemeSettings {
        return repository.switchTheme(isDarkTheme)
    }

    override fun isDarkThemeSaved(): Boolean {
        return repository .isDarkThemeSaved()
    }
}