package com.example.playlist_maker.presentation.mediaLibrary.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlist_maker.R
import com.example.playlist_maker.databinding.FragmentMediaLibraryBinding
import com.example.playlist_maker.presentation.mediaLibrary.view_model.MediaLibraryViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaLibraryFragment : Fragment(R.layout.fragment_media_library) {

    private val mediaLibraryViewModel: MediaLibraryViewModel by viewModel()
    private var _binding: FragmentMediaLibraryBinding? = null
    private val binding get() = _binding!!
    private var tabMediator: TabLayoutMediator? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMediaLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabTitles = listOf(getString(
            R.string.tab_favorites),
            getString(R.string.tab_playlists)
        )

        binding.viewPagerML.adapter = MediaLibraryAdapter(this)

        tabMediator = TabLayoutMediator(binding.tabLayoutML, binding.viewPagerML) { tab, position ->
                tab.text = tabTitles[position]
        }
        tabMediator?.attach()

        mediaLibraryViewModel.mediaLibrary.observe(viewLifecycleOwner) { mediaLibrary ->
            binding.viewPagerML.setCurrentItem(mediaLibrary.selectedTabIndex, false)
            binding.tabLayoutML.getTabAt(mediaLibrary.selectedTabIndex)?.select()
        }

        binding.tabLayoutML.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    mediaLibraryViewModel.selectedTabIndex(it.position)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator?.detach()
    }
}