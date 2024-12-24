package com.example.playlist_maker.presentation.search.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlist_maker.R
import com.example.playlist_maker.databinding.FragmentSearchBinding
import com.example.playlist_maker.presentation.audioPlayer.activity.AudioPlayer
import com.example.playlist_maker.presentation.search.view_model.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment(R.layout.fragment_search) {

    private val searchViewModel: SearchViewModel by viewModel()
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var trackAdapter: TrackAdapter
    private lateinit var searchHistoryAdapter: SearchHistoryAdapter

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val KEY_SEARCH_TEXT = "searchText"
        private const val CLICK_DEBOUNCE_DELAY = 500L

        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }

    private val searchHandler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { searchViewModel.search(searchText) }
    private val clickHandler = Handler(Looper.getMainLooper())
    private var clickRunnable: Runnable? = null
    private var searchText: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerSearchHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        searchHistoryAdapter = SearchHistoryAdapter(emptyList()) { track ->
            val intent = Intent(requireContext(), AudioPlayer::class.java).apply {
                putExtra("track_name", track.trackName)
                putExtra("artist_name", track.artistName)
                putExtra("track_time", track.trackTimeMillis)
                putExtra("collection_name", track.collectionName)
                putExtra("release_date", track.getFormattedReleaseYear(track.releaseDate))
                putExtra("primary_genre_name", track.primaryGenreName)
                putExtra("country", track.country)
                putExtra("preview_url", track.previewUrl)
                putExtra("cover_artwork", track.getCoverArtwork())
            }
            startActivity(intent)
        }

        binding.recyclerSearchHistory.adapter = searchHistoryAdapter

        trackAdapter = TrackAdapter(emptyList()) { track ->
            clickRunnable?.let { clickHandler.removeCallbacks(it) }
            clickRunnable = Runnable {
                searchViewModel.addTrackToHistory(track)
                val intent = Intent(requireContext(), AudioPlayer::class.java).apply {
                    putExtra("track_name", track.trackName)
                    putExtra("artist_name", track.artistName)
                    putExtra("track_time", track.trackTimeMillis)
                    putExtra("collection_name", track.collectionName)
                    putExtra("release_date", track.getFormattedReleaseYear(track.releaseDate))
                    putExtra("primary_genre_name", track.primaryGenreName)
                    putExtra("country", track.country)
                    putExtra("preview_url", track.previewUrl)
                    putExtra("cover_artwork", track.getCoverArtwork())
                }
                startActivity(intent)
            }
            clickRunnable?.let { clickHandler.postDelayed(it, CLICK_DEBOUNCE_DELAY) }
        }

        binding.recyclerView.adapter = trackAdapter

        val searchClearButton = binding.clearButton
        val buttonUpdate = binding.buttonUpdate
        val searchEditText = binding.searchEditText
        if (savedInstanceState != null) {
            searchText = savedInstanceState.getString(KEY_SEARCH_TEXT, "")
            searchEditText.setText(searchText)
        }

        searchEditText.addTextChangedListener(
            onTextChanged = { charSequence, _, _, _ ->
                searchClearButton.isVisible = !charSequence.isNullOrEmpty()
                trackAdapter.updateTracks(emptyList())
                val newSearchText = charSequence.toString()
                if (searchText != newSearchText) {
                    searchText = newSearchText
                    searchViewModel.updateSearchText(searchText)
                    searchDebounce()
                }
            })

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                closeKeyboard(requireContext(), searchEditText)
                true
            } else {
                false
            }
        }
        searchViewModel.screenState.observe(viewLifecycleOwner) { state ->
            searchHistoryAdapter.updateTracks(state.historyList)
            trackAdapter.updateTracks(state.trackList)
            binding.progressBar.isVisible = state.isLoading
            binding.noResults.isVisible = state.noResults
            binding.noInternet.isVisible = state.noInternet
            binding.recyclerView.isVisible = !state.noInternet
            buttonUpdate.isVisible = state.noInternet
            if (state.searchText != searchText) {
                searchText = state.searchText
                if (searchEditText.text.toString() != state.searchText) {
                    searchEditText.setText(searchText)
                }
            }
            binding.searchHistoryContainer.isVisible =
                state.searchHistoryVisible && state.isSearchFocused && state.searchText.isEmpty() && state.historyList.isNotEmpty()
        }

        searchClearButton.setOnClickListener {
            searchViewModel.clearSearch()
        }

        binding.clearHistoryButton.setOnClickListener {
            searchViewModel.clearHistory()
        }

        searchEditText.setOnFocusChangeListener { _, hasFocus ->
            searchViewModel.onSearchEditTextFocusChanged(hasFocus)
        }

        buttonUpdate.setOnClickListener {
            searchViewModel.retryLastSearch()
        }
    }

    private fun searchDebounce() {
        searchHandler.removeCallbacks(searchRunnable)
        searchHandler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_SEARCH_TEXT, searchText)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let {
            searchText = it.getString(KEY_SEARCH_TEXT, "")
            if (binding.searchEditText.text.toString() != searchText) {
                binding.searchEditText.setText(searchText)
                searchViewModel.updateSearchText(searchText)
            }
        }
    }

    private fun closeKeyboard(context: Context, searchEditText: EditText) {
        val closeKeyboard =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        closeKeyboard.hideSoftInputFromWindow(searchEditText.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}