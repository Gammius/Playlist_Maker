package com.example.playlist_maker.presentation.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlist_maker.R
import com.example.playlist_maker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (
                destination.id == R.id.newPlaylistFragment ||
                destination.id == R.id.playlistFragment ||
                destination.id == R.id.playlistEditFragment
            ) {
                binding.bottomNavigationView.visibility = View.GONE
            } else {
                binding.bottomNavigationView.visibility = View.VISIBLE
            }
        }

        binding.bottomNavigationView.setupWithNavController(navController)

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.searchFragment -> navController.navigate(R.id.searchFragment)
                R.id.mediaLibraryFragment -> navController.navigate(R.id.mediaLibraryFragment)
                R.id.settingsFragment -> navController.navigate(R.id.settingsFragment)
            }
            true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}