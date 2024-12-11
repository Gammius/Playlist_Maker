package com.example.playlist_maker

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlist_maker.Creator.Creator
import com.example.playlist_maker.domain.settings.SettingsInteractor

class App : Application() {

    lateinit var settingsInteractor: SettingsInteractor

    override fun onCreate() {
        super.onCreate()
        settingsInteractor = Creator.providerSettingsInteractor(this)

        if (!settingsInteractor.isDarkThemeSaved()) {
            val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            val isDarkMode =currentNightMode == Configuration.UI_MODE_NIGHT_YES
            settingsInteractor.switchTheme(isDarkMode)
        } else {
            settingsInteractor.switchTheme(settingsInteractor.darkTheme)
        }
        updateTheme(settingsInteractor.darkTheme)
    }

    private fun updateTheme(isDarkTheme: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}