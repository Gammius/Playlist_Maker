package com.example.playlist_maker.data

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlist_maker.domain.api.SettingsRepository

const val APP_PREFERENCES = "app_preferences"
const val DARK_THEME = "dark_theme"

class SettingsRepositoryImpl(context: Context) : SettingsRepository {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(APP_PREFERENCES,Context.MODE_PRIVATE)

    override var darkTheme: Boolean
        get() = sharedPreferences.getBoolean(DARK_THEME,false)
        set(value) {
            switchTheme(value)
        }
    fun isDarkThemeSaved(): Boolean {
        return sharedPreferences.contains(DARK_THEME)
    }

    override fun switchTheme(isDarkTheme: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(DARK_THEME, isDarkTheme)
        editor.apply()
        updateTheme(isDarkTheme)
    }

    private fun updateTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}