package com.example.playlist_maker

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate

const val APP_PREFERENCES = "app_preferences"
const val DARK_THEME = "dark_theme"

class App : Application() {

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        val sharedPreferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        darkTheme = sharedPreferences.getBoolean(DARK_THEME, false)
        if (sharedPreferences.contains(DARK_THEME).not()) {
            val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            darkTheme = currentNightMode == Configuration.UI_MODE_NIGHT_YES
        }
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        val sharedPreferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(DARK_THEME, darkThemeEnabled)
        editor.apply()
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}