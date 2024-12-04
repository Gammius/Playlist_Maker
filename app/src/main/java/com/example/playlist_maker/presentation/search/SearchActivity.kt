package com.example.playlist_maker.presentation.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlist_maker.Creator
import com.example.playlist_maker.R
import com.example.playlist_maker.data.TrackRepositoryImpl
import com.example.playlist_maker.presentation.audioPlayer.AudioPlayer
import com.example.playlist_maker.domain.models.Track
import com.example.playlist_maker.data.dto.TrackResponse
import com.example.playlist_maker.data.network.RetrofitNetworkClient
import com.example.playlist_maker.domain.api.SearchHistoryInteractor
import com.example.playlist_maker.domain.api.TrackInteractor
import com.example.playlist_maker.domain.impl.TrackInteractorImpl
import retrofit2.Call

class SearchActivity : AppCompatActivity() {
    private var currentCall: Call<TrackResponse>? = null
    private val trackInteractor: TrackInteractor = Creator.provideTrackInteractor()
    private lateinit var searchHistoryInteractor: SearchHistoryInteractor
    private var searchText: String = ""
    private lateinit var searchEditText: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var noResultsView: LinearLayout
    private lateinit var noInternetView: LinearLayout
    private lateinit var buttonUpdate: Button
    private lateinit var searchHistoryContainer: LinearLayout
    private var lastFailedQuery: String? = null
    private lateinit var recyclerSearchHistory: RecyclerView
    private lateinit var searchHistoryAdapter: SearchHistoryAdapter
    private lateinit var currentTrack: AudioPlayer

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val KEY_SEARCH_TEXT = "searchText"
        private const val CLICK_DEBOUNCE_DELAY = 500L
    }

    private val searchHandler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { filterTracks(searchText) }
    private val clickHandler = Handler(Looper.getMainLooper())
    private var clickRunnable: Runnable? = null

    object TrackHolder {
        var selectedTrack: Track? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        searchEditText = findViewById(R.id.search_edit_text)
        searchHistoryInteractor = Creator.provideSearchHistoryInteractor(applicationContext)
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
            clickRunnable = Runnable{
                searchHistoryInteractor.addTrack(track)
                updateSearchHistory()
                TrackHolder.selectedTrack = track
                val intent = Intent(this, AudioPlayer::class.java)
                startActivity(intent)
            }
            clickRunnable?.let { clickHandler.postDelayed(it, CLICK_DEBOUNCE_DELAY) }
        }
        recyclerView.adapter = trackAdapter
        searchHistoryContainer = findViewById(R.id.search_history_container)
        updateSearchHistory()

        val buttonBackSettings = findViewById<Button>(R.id.arrow_back)
        buttonBackSettings.setOnClickListener {
            onBackPressed()
        }

        val searchClearButton = findViewById<ImageView>(R.id.clear_button)
        if (savedInstanceState != null) {
            searchText = savedInstanceState.getString(KEY_SEARCH_TEXT, "")
            searchEditText.setText(searchText)
        }

        searchClearButton.setOnClickListener {
            searchEditText.setText("")
            closeKeyboard(searchEditText.context, searchEditText)
            searchClearButton.visibility = View.GONE
        }

        val clearSearchHistoryButton = findViewById<Button>(R.id.clear_history_button)
        clearSearchHistoryButton.setOnClickListener {
            searchHistoryInteractor.clearHistory()
            updateSearchHistory()
        }

        searchEditText.addTextChangedListener(
            onTextChanged = { charSequence, _, _, _ ->
                searchClearButton.isVisible = !charSequence.isNullOrEmpty()
                trackAdapter.updateTracks(emptyList())
                progressBar.visibility = View.VISIBLE
                searchText = charSequence.toString()
                searchDebounce()
                updateSearchHistory()
            })

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                filterTracks(searchText)
                closeKeyboard(this, searchEditText)
                true
            } else {
                false
            }
        }

        searchEditText.setOnFocusChangeListener { _, hasFocus ->
            updateSearchHistory()
        }

        buttonUpdate.setOnClickListener {
            lastFailedQuery?.let { query -> filterTracks(query) }
        }
    }

    private fun searchDebounce() {
        searchHandler.removeCallbacks(searchRunnable)
        searchHandler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun filterTracks(query: String) {
        currentCall?.cancel()

        if (query.isEmpty()) {
            noResultsView.visibility =
                if (query.isEmpty() && searchEditText.text.isNotEmpty()) View.VISIBLE else View.GONE
            noInternetView.isVisible = false
            recyclerView.isVisible = false
            return
        }

        trackInteractor.search(query, object : TrackInteractor.TrackConsumer {
            override fun consume(trackList: List<Track>) {
                progressBar.visibility = View.GONE
                if (trackList.isNotEmpty()) {

                    trackAdapter.updateTracks(trackList)
                    noResultsView.isVisible = trackList.isEmpty()
                    recyclerView.isVisible = trackList.isNotEmpty()
                    noInternetView.isVisible = false
                } else {
                    noResultsView.isVisible = false
                    recyclerView.isVisible = false
                    noInternetView.isVisible = true
                }
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_SEARCH_TEXT, searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString(KEY_SEARCH_TEXT, "")
        searchEditText.setText(searchText)
    }

    private fun closeKeyboard(context: Context, searchEditText: EditText) {
        val closeKeyboard =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        closeKeyboard.hideSoftInputFromWindow(searchEditText.windowToken, 0)
    }

    private fun updateSearchHistory() {
        val history = searchHistoryInteractor.getHistory()
        searchHistoryAdapter.updateTracks(history)
        searchHistoryContainer.visibility =
            if (history.isNotEmpty() && searchEditText.hasFocus()
                && searchEditText.text.isEmpty()
            ) View.VISIBLE else View.GONE
    }
}