package com.example.playlist_maker.data.settings.impl

import android.content.SharedPreferences
import com.example.playlist_maker.domain.settings.SettingsRepository
import com.example.playlist_maker.domain.settings.model.ThemeSettings

const val APP_PREFERENCES = "app_preferences"
const val DARK_THEME = "dark_theme"

class SettingsRepositoryImpl(private val sharedPreferences: SharedPreferences) : SettingsRepository {

    override var darkTheme: Boolean
        get() = sharedPreferences.getBoolean(DARK_THEME,false)
        set(value) {
            switchTheme(value)
        }
    override fun isDarkThemeSaved(): Boolean {
        return sharedPreferences.contains(DARK_THEME)
    }

    override fun switchTheme(isDarkTheme: Boolean) : ThemeSettings {
        val editor = sharedPreferences.edit()
        editor.putBoolean(DARK_THEME, isDarkTheme)
        editor.apply()
        return ThemeSettings(isDarkTheme)
    }
}