package com.example.practicum.playlist.ui.settings.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlist_maker.domain.settings.SettingsInteractor
import com.example.playlist_maker.domain.settings.model.ThemeSettings
import com.example.playlist_maker.domain.sharing.SharingInteractor
import com.example.playlist_maker.domain.sharing.model.EmailData

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : ViewModel() {

    private val _themeSettings = MutableLiveData<ThemeSettings>()
    val themeSettings: LiveData<ThemeSettings> get() = _themeSettings

    init {
        _themeSettings.value = ThemeSettings(isDarkTheme = settingsInteractor.darkTheme)
    }

    fun switchTheme(isDarkTheme: Boolean) {
        settingsInteractor.switchTheme(isDarkTheme)
        _themeSettings.value = ThemeSettings(isDarkTheme = isDarkTheme)
    }

    fun shareAppLink(messageIconRound: String) {
        sharingInteractor.shareLink(messageIconRound)
    }

    fun openSupportLink(urlIconArrow: String) {
        sharingInteractor.openLink(urlIconArrow)
    }

    fun openSupportEmail(emailData: EmailData) {
        sharingInteractor.openEmail(emailData)
    }
}