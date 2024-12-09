package com.example.playlist_maker.presentation.settings.activity

import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.playlist_maker.Creator.Creator
import com.example.playlist_maker.R
import com.example.playlist_maker.presentation.settings.view_model.SettingsViewModelFactory
import com.example.practicum.playlist.ui.settings.view_model.SettingsViewModel
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : ComponentActivity() {

    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            SettingsViewModelFactory(
                sharingInteractor = Creator.providerSharingInteractor(this),
                settingsInteractor = Creator.providerSettingsInteractor(this),
                context = applicationContext
            )
        ).get(SettingsViewModel::class.java)

        setContentView(R.layout.activity_settings)

        val buttonBackSettings = findViewById<Button>(R.id.arrow_back)
        buttonBackSettings.setOnClickListener {
            onBackPressed()
        }

        val buttonIconRound = findViewById<Button>(R.id.icon_round)
        buttonIconRound.setOnClickListener {
            viewModel.shareAppLink()
        }

        val buttonIconCall = findViewById<Button>(R.id.icon_call)
        buttonIconCall.setOnClickListener {
            viewModel.openSupportEmail()
        }

        val buttonIconArrow = findViewById<Button>(R.id.arrow)
        buttonIconArrow.setOnClickListener {
            viewModel.openSupportLink()
        }

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        viewModel.themeSettings.observe(this) { themeSettings ->
            themeSwitcher.isChecked = themeSettings.isDarkTheme
            updateTheme(themeSettings.isDarkTheme)
            applySwitchColors(themeSwitcher)
        }

        themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            viewModel.switchTheme(isChecked)
            updateTheme(isChecked)
            applySwitchColors(themeSwitcher)
        }
    }

    private fun applySwitchColors(switcher: SwitchMaterial) {
        val thumbColor = if (switcher.isChecked) R.color.blue else R.color.icon_color2
        val trackColor = if (switcher.isChecked) R.color.trackTintNight else R.color.trackTint
        switcher.thumbTintList = ColorStateList.valueOf(ContextCompat.getColor(this, thumbColor))
        switcher.trackTintList = ColorStateList.valueOf(ContextCompat.getColor(this, trackColor))
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