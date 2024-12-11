package com.example.playlist_maker.presentation.search.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlist_maker.Creator.Creator
import com.example.playlist_maker.R
import com.example.playlist_maker.domain.search.model.Track
import com.example.playlist_maker.presentation.audioPlayer.activity.AudioPlayer
import com.example.playlist_maker.presentation.search.view_model.SearchViewModel
import com.example.playlist_maker.presentation.search.view_model.SearchViewModelFactory

class SearchActivity : AppCompatActivity() {

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var searchEditText: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var noResultsView: LinearLayout
    private lateinit var noInternetView: LinearLayout
    private lateinit var buttonUpdate: Button
    private lateinit var searchHistoryContainer: LinearLayout
    private lateinit var recyclerSearchHistory: RecyclerView
    private lateinit var searchHistoryAdapter: SearchHistoryAdapter

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val KEY_SEARCH_TEXT = "searchText"
        private const val CLICK_DEBOUNCE_DELAY = 500L
    }

    private val searchHandler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { searchViewModel.search(searchText) }
    private val clickHandler = Handler(Looper.getMainLooper())
    private var clickRunnable: Runnable? = null
    private var searchText: String = ""

    object TrackHolder {
        var selectedTrack: Track? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchViewModel = ViewModelProvider(
            this, SearchViewModelFactory(
                application = application,
                trackInteractor = Creator.provideTrackInteractor(),
                searchHistoryInteractor = Creator.provideSearchHistoryInteractor(this)
            )
        ).get(SearchViewModel::class.java)

        setContentView(R.layout.activity_search)
        searchEditText = findViewById(R.id.search_edit_text)
        recyclerSearchHistory = findViewById(R.id.recycler_search_history)
        recyclerSearchHistory.layoutManager = LinearLayoutManager(this)

        searchHistoryAdapter = SearchHistoryAdapter(emptyList()) { track ->
            TrackHolder.selectedTrack = track
            val intent = Intent(this, AudioPlayer::class.java)
            startActivity(intent)
        }

        recyclerSearchHistory.adapter = searchHistoryAdapter
        progressBar = findViewById(R.id.progressBar)
        noResultsView = findViewById(R.id.no_results)
        noInternetView = findViewById(R.id.no_internet)
        buttonUpdate = findViewById(R.id.button_update)
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        trackAdapter = TrackAdapter(emptyList()) { track ->
            clickRunnable?.let { clickHandler.removeCallbacks(it) }
            clickRunnable = Runnable {
                searchViewModel.addTrackToHistory(track)
                TrackHolder.selectedTrack = track
                val intent = Intent(this, AudioPlayer::class.java)
                startActivity(intent)
            }
            clickRunnable?.let { clickHandler.postDelayed(it, CLICK_DEBOUNCE_DELAY) }
        }

        recyclerView.adapter = trackAdapter

        searchHistoryContainer = findViewById(R.id.search_history_container)

        val searchClearButton = findViewById<ImageView>(R.id.clear_button)
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
                closeKeyboard(this, searchEditText)
                true
            } else {
                false
            }
        }
        searchViewModel.screenState.observe(this) { state ->
            searchHistoryAdapter.updateTracks(state.historyList)
            trackAdapter.updateTracks(state.trackList)
            progressBar.isVisible = state.isLoading
            noResultsView.isVisible = state.noResults
            noInternetView.isVisible = state.noInternet
            recyclerView.isVisible = !state.noInternet
            buttonUpdate.isVisible = state.noInternet
            if (state.searchText != searchText) {
                searchText = state.searchText
                if (searchEditText.text.toString() != state.searchText) {
                    searchEditText.setText(searchText)
                }
            }
            searchHistoryContainer.isVisible =
                state.searchHistoryVisible && !state.isSearchFocused && state.searchText.isEmpty()
            if (state.isSearchFocused) {
                searchHistoryContainer.isVisible = false
            } else if (state.searchText.isEmpty()) {
                searchHistoryContainer.isVisible = true
            }
        }

        searchClearButton.setOnClickListener {
            searchViewModel.clearSearch()
        }

        val clearSearchHistoryButton = findViewById<Button>(R.id.clear_history_button)
        clearSearchHistoryButton.setOnClickListener {
            searchViewModel.clearHistory()
        }

        val buttonBackSettings = findViewById<Button>(R.id.arrow_back)
        buttonBackSettings.setOnClickListener {
            onBackPressed()
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

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString(KEY_SEARCH_TEXT, "")
        if (searchEditText.text.toString() != searchText) {
            searchEditText.setText(searchText)
            searchViewModel.updateSearchText(searchText)
        }
    }

    private fun closeKeyboard(context: Context, searchEditText: EditText) {
        val closeKeyboard =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        closeKeyboard.hideSoftInputFromWindow(searchEditText.windowToken, 0)
    }
}