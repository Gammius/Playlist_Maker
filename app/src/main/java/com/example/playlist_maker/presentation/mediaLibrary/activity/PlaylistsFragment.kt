package com.example.playlist_maker.presentation.mediaLibrary.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlist_maker.R
import com.example.playlist_maker.Utils.GridSpacingItemDecoration
import com.example.playlist_maker.databinding.FragmentPlaylistsBinding
import com.example.playlist_maker.presentation.mediaLibrary.view_model.MediaLibraryViewModel
import com.example.playlist_maker.presentation.mediaLibrary.view_model.PlaylistViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private val playlistsViewModel: PlaylistViewModel by viewModel()
    private val mediaLibraryViewModel: MediaLibraryViewModel by viewModel()
    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!
    private lateinit var playlistAdapter: PlaylistAdapter

    companion object {
        fun newInstance(): PlaylistsFragment {
            return PlaylistsFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fun dpToPx(dp: Int): Int {
            val density = resources.displayMetrics.density
            return (dp * density).toInt()
        }

        val spacing = dpToPx(4)
        val leftRightSpacing = dpToPx(16)
        val topSpacing = dpToPx(16)

        binding.recyclerViewPlaylist.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerViewPlaylist.addItemDecoration(
            GridSpacingItemDecoration(2, spacing, leftRightSpacing, topSpacing)
        )

        playlistAdapter = PlaylistAdapter(emptyList())

        binding.recyclerViewPlaylist.adapter = playlistAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                playlistsViewModel.playlist.collect { playlist ->
                    binding.noPlaylist.isVisible = playlist.noPlaylist ?: true
                    playlistAdapter.updatePlaylist(playlist.playlists)
                }
            }
        }
        binding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.action_playlistFragment_to_newPlaylistFragment)
            mediaLibraryViewModel.selectedTabIndex(1)
        }
    }

    override fun onResume() {
        super.onResume()
        playlistsViewModel.loadPlaylist()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}