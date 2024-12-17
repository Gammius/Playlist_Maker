package com.example.playlist_maker.presentation.settings.activity

import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.example.playlist_maker.R
import com.example.playlist_maker.domain.sharing.model.EmailData
import com.example.practicum.playlist.ui.settings.view_model.SettingsViewModel
import com.google.android.material.switchmaterial.SwitchMaterial
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {
    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val messageIconRound = getString(R.string.message_icon_round)
        val urlIconArrow = getString(R.string.url_icon_arrow)
        val mailUserIconCall = getString(R.string.mail_user_icon_call)
        val subjectIconCall = getString(R.string.subject_icon_call)
        val messageIconCall = getString(R.string.message_icon_call)

        setContentView(R.layout.activity_settings)

        val buttonBackSettings = findViewById<Button>(R.id.arrow_back)
        buttonBackSettings.setOnClickListener {
            onBackPressed()
        }

        val buttonIconRound = findViewById<Button>(R.id.icon_round)
        buttonIconRound.setOnClickListener {
            viewModel.shareAppLink(messageIconRound)
        }

        val buttonIconCall = findViewById<Button>(R.id.icon_call)
        buttonIconCall.setOnClickListener {
            val emailData = EmailData(
                email = mailUserIconCall,
                subject = subjectIconCall,
                body = messageIconCall
            )
            viewModel.openSupportEmail(emailData)
        }

        val buttonIconArrow = findViewById<Button>(R.id.arrow)
        buttonIconArrow.setOnClickListener {
            viewModel.openSupportLink(urlIconArrow)
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