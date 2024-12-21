package com.example.playlist_maker.presentation.mediaLibrary.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlist_maker.R
import com.example.playlist_maker.databinding.ActivityMediaLibraryBinding
import com.example.playlist_maker.presentation.mediaLibrary.view_model.MediaLibraryViewModel
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaLibraryActivity : AppCompatActivity() {

    private val mediaLibraryViewModel: MediaLibraryViewModel by viewModel()
    private lateinit var binding: ActivityMediaLibraryBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMediaLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tabTitles = listOf(getString(
            R.string.tab_favorites),
            getString(R.string.tab_playlists)
        )

        binding.arrowBack.setOnClickListener {
            onBackPressed()
        }

        binding.viewPagerML.adapter = MediaLibraryAdapter(this)

        tabMediator = TabLayoutMediator(binding.tabLayoutML, binding.viewPagerML) { tab, position ->
                tab.text = tabTitles[position]
        }
        tabMediator.attach()

        mediaLibraryViewModel.mediaLibrary.observe(this) { mediaLibrary ->
            binding.viewPagerML.setCurrentItem(mediaLibrary.selectedTabIndex, false)
            binding.tabLayoutML.getTabAt(mediaLibrary.selectedTabIndex)?.select()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}