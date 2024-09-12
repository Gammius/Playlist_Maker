package com.example.playlist_maker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val buttonBackSettings = findViewById<Button>(R.id.arrow_back)
        buttonBackSettings.setOnClickListener {
            onBackPressed()
        }

        val buttonIconRound = findViewById<Button>(R.id.icon_round)
        buttonIconRound.setOnClickListener {
            val ShareAppIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, getString(R.string.message_icon_round))
            }
            startActivity(ShareAppIntent)
        }

        val buttonIconCall = findViewById<Button>(R.id.icon_call)
        buttonIconCall.setOnClickListener {
            val iconCallIntent = Intent(Intent.ACTION_SENDTO)
            iconCallIntent.data = Uri.parse("mailto:")
            iconCallIntent.putExtra(
                Intent.EXTRA_EMAIL,
                arrayOf(getString(R.string.mail_user_icon_call))
            )
            iconCallIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject_icon_call))
            iconCallIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.message_icon_call))
            startActivity(iconCallIntent)
        }

        val buttonIconArrow = findViewById<Button>(R.id.arrow)
        buttonIconArrow.setOnClickListener {
            val iconArrowIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_icon_arrow)))
            startActivity(iconArrowIntent)
        }

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)

        themeSwitcher.isChecked = (applicationContext as App).darkTheme

        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
        }
    }
}