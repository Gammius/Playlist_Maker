package com.example.playlist_maker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.playlist_maker.R

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val buttonBackSettings = findViewById<Button>(R.id.arrow_back)
        buttonBackSettings.setOnClickListener{
            val intentBackSettings = Intent(this, MainActivity::class.java)
            startActivity(intentBackSettings)
        }
    }
}