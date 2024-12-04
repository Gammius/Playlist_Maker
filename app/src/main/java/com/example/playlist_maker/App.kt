package com.example.playlist_maker

import android.app.Application
import android.content.res.Configuration
import com.example.playlist_maker.domain.api.SettingsInteractor
import com.example.playlist_maker.domain.impl.SettingsInteractorImpl

class App : Application() {

    lateinit var settingsInteractor: SettingsInteractor

    override fun onCreate() {
        super.onCreate()
        settingsInteractor = Creator.providerSettingsInteractor(this)

        if (!(settingsInteractor as SettingsInteractorImpl).isDarkThemeSaved()) {
            val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            val isDarkMode =currentNightMode == Configuration.UI_MODE_NIGHT_YES
            settingsInteractor.switchTheme(isDarkMode)
        } else {
            settingsInteractor.switchTheme(settingsInteractor.darkTheme)
        }
    }
}