package com.example.playlist_maker.presentation.settings.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlist_maker.domain.settings.SettingsInteractor
import com.example.playlist_maker.domain.sharing.SharingInteractor
import com.example.practicum.playlist.ui.settings.view_model.SettingsViewModel

class SettingsViewModelFactory(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(sharingInteractor, settingsInteractor,context) as T
    }
}