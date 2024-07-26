package com.example.playlist_maker
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button


class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val buttonBackSettings = findViewById<Button>(R.id.arrow_back)
        buttonBackSettings.setOnClickListener{
            onBackPressed()
        }
    }
}