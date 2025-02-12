package com.example.playlist_maker.presentation.mediaLibrary.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlist_maker.databinding.FragmentFavoritesBinding
import com.example.playlist_maker.domain.search.model.Track
import com.example.playlist_maker.presentation.audioPlayer.activity.AudioPlayer
import com.example.playlist_maker.presentation.mediaLibrary.view_model.FavoritesViewModel
import com.example.playlist_maker.presentation.search.activity.TrackAdapter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {

    private val favoritesViewModel: FavoritesViewModel by viewModel()
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private lateinit var trackAdapter: TrackAdapter

    companion object {
        fun newInstance(): FavoritesFragment {
            return FavoritesFragment()
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

        binding.recyclerViewFavorite.layoutManager = LinearLayoutManager(requireContext())

        trackAdapter = TrackAdapter(emptyList()) { track ->
            val intent = createTrackIntent(track)
            startActivity(intent)
        }

        binding.recyclerViewFavorite.adapter = trackAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                favoritesViewModel.favorites.collect { favorites ->
                    binding.noMedia.isVisible = favorites.noMedia ?: true
                    trackAdapter.updateTracks(favorites.trackList)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        favoritesViewModel.loadFavoriteTracks()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createTrackIntent(track: Track): Intent {
        return Intent(requireContext(), AudioPlayer::class.java).apply {
            putExtra("track_id", track.trackId)
            putExtra("track_name", track.trackName)
            putExtra("artist_name", track.artistName)
            putExtra("track_time", track.trackTimeMillis)
            putExtra("collection_name", track.collectionName)
            putExtra("release_date", track.getFormattedReleaseYear(track.releaseDate))
            putExtra("primary_genre_name", track.primaryGenreName)
            putExtra("country", track.country)
            putExtra("preview_url", track.previewUrl)
            putExtra("cover_artwork", track.getCoverArtwork())
            putExtra("is_favorite", track.isFavorite)
        }
    }
}