package com.example.practicum.playlist.ui.settings.view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlist_maker.R
import com.example.playlist_maker.domain.settings.SettingsInteractor
import com.example.playlist_maker.domain.settings.model.ThemeSettings
import com.example.playlist_maker.domain.sharing.SharingInteractor
import com.example.playlist_maker.domain.sharing.model.EmailData

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
    private val context: Context
) : ViewModel() {

    private val _themeSettings = MutableLiveData<ThemeSettings>()
    val themeSettings: LiveData<ThemeSettings> get() = _themeSettings

    init {
        val currentTheme = settingsInteractor.darkTheme
        _themeSettings.value = ThemeSettings(isDarkTheme = currentTheme)
    }

    fun switchTheme(isDarkTheme: Boolean) {
        settingsInteractor.switchTheme(isDarkTheme)
        _themeSettings.value = ThemeSettings(isDarkTheme = settingsInteractor.darkTheme)
    }

    fun shareAppLink() {
        val link = context.getString(R.string.message_icon_round)
        sharingInteractor.shareLink(link)
    }

    fun openSupportLink() {
        val link = context.getString(R.string.url_icon_arrow)
        sharingInteractor.openLink(link)
    }

    fun openSupportEmail() {
        val emailData = EmailData(
            email = context.getString(R.string.mail_user_icon_call),
            subject = context.getString(R.string.subject_icon_call),
            body = context.getString(R.string.message_icon_call)
        )
        sharingInteractor.openEmail(emailData)
    }
}