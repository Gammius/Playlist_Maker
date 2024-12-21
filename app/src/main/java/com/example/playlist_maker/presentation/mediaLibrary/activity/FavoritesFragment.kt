package com.example.playlist_maker.presentation.mediaLibrary.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.playlist_maker.databinding.FragmentFavoritesBinding
import com.example.playlist_maker.presentation.mediaLibrary.view_model.FavoritesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {

    private val favoritesViewModel: FavoritesViewModel by viewModel()
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(): FavoritesFragment {
            return FavoritesFragment().apply {
                arguments = Bundle().apply {
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoritesViewModel.favorites.observe(viewLifecycleOwner) { favorites ->
            binding.noMedia.isVisible = favorites.noMedia
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}