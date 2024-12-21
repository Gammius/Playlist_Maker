package com.example.playlist_maker.presentation.mediaLibrary.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.playlist_maker.databinding.FragmentPlaylistsBinding
import com.example.playlist_maker.presentation.mediaLibrary.view_model.PlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private val playlistsViewModel: PlaylistViewModel by viewModel()
    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(): PlaylistsFragment {
            return PlaylistsFragment().apply {
                arguments = Bundle().apply {
                }
            }
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

        playlistsViewModel.playlist.observe(viewLifecycleOwner) { playlist ->
            binding.noPlaylist.isVisible = playlist.noPlaylist
        }

        binding.newPlaylistButton.setOnClickListener {
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}