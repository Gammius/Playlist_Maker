package com.example.playlist_maker

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlist_maker.di.dataModule
import com.example.playlist_maker.di.interactorModule
import com.example.playlist_maker.di.repositoryModule
import com.example.playlist_maker.di.viewModelModule
import com.example.playlist_maker.domain.settings.SettingsInteractor
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(listOf( dataModule, repositoryModule, interactorModule, viewModelModule))
        }

        val settingsInteractor: SettingsInteractor by inject()
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