package com.example.playlist_maker.domain.impl

import com.example.playlist_maker.data.SettingsRepositoryImpl
import com.example.playlist_maker.domain.api.SettingsInteractor
import com.example.playlist_maker.domain.api.SettingsRepository

class SettingsInteractorImpl (private val repository: SettingsRepository) : SettingsInteractor {
    override var darkTheme: Boolean
        get() = repository.darkTheme
        set(value) {
            repository.switchTheme(value)
        }

    override fun switchTheme(isDarkTheme: Boolean) {
        repository.switchTheme(isDarkTheme)
    }

    override fun isDarkThemeSaved(): Boolean {
        return repository .isDarkThemeSaved()
    }
}