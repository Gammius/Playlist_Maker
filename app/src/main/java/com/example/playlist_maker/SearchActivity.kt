package com.example.playlist_maker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlist_maker.AudioPlayer.AudioPlayer
import com.example.playlist_maker.track.Track
import com.example.playlist_maker.track.TrackAdapter
import com.example.playlist_maker.track.TrackResponse
import com.example.playlist_maker.track.trackApi
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    private var currentCall: Call<TrackResponse>? = null
    private val trackBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(trackBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val trackService = retrofit.create(trackApi::class.java)
    private var searchText: String = ""
    private lateinit var searchEditText: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var noResultsView: LinearLayout
    private lateinit var noInternetView: LinearLayout
    private lateinit var buttonUpdate: Button
    private lateinit var searchHistoryContainer: LinearLayout
    private var lastFailedQuery: String? = null
    private lateinit var searchHistory: SearchHistory
    private lateinit var recyclerSearchHistory: RecyclerView
    private lateinit var searchHistoryAdapter: SearchHistoryAdapter
    private lateinit var currentTrack: AudioPlayer

    object TrackHolder {
        var selectedTrack: Track? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        searchEditText = findViewById(R.id.search_edit_text)
        searchHistory = SearchHistory(this)
        recyclerSearchHistory = findViewById(R.id.recycler_search_history)
        recyclerSearchHistory.layoutManager = LinearLayoutManager(this)
        searchHistoryAdapter = SearchHistoryAdapter(emptyList()) { track ->
            TrackHolder.selectedTrack = track
            val intent = Intent(this, AudioPlayer::class.java)
            startActivity(intent)
        }
        recyclerSearchHistory.adapter = searchHistoryAdapter
        noResultsView = findViewById(R.id.no_results)
        noInternetView = findViewById(R.id.no_internet)
        buttonUpdate = findViewById(R.id.button_update)
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)


        trackAdapter = TrackAdapter(emptyList()) { track ->
            searchHistory.addTrack(track)
            updateSearchHistory()

            TrackHolder.selectedTrack = track
            val intent = Intent(this, AudioPlayer::class.java)
            startActivity(intent)
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
            searchHistory.clearHistory()
            updateSearchHistory()
        }

        searchEditText.addTextChangedListener(
            onTextChanged = { charSequence, _, _, _ ->
                searchClearButton.isVisible = !charSequence.isNullOrEmpty()
                searchText = charSequence.toString()
                filterTracks(charSequence.toString())

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

    private fun filterTracks(query: String) {
        currentCall?.cancel()
        if (query.isEmpty()) {
            trackAdapter.updateTracks(emptyList())
            noResultsView.visibility =
                if (query.isEmpty() && searchEditText.text.isNotEmpty()) View.VISIBLE else View.GONE
            noInternetView.isVisible = false
            recyclerView.isVisible = false
            return
        }

        currentCall = trackService.search(query)
        currentCall?.enqueue(object : retrofit2.Callback<TrackResponse> {
            override fun onResponse(
                call: Call<TrackResponse>,
                response: retrofit2.Response<TrackResponse>
            ) {
                if (response.isSuccessful) {
                    val trackList = response.body()?.results ?: emptyList()
                    trackAdapter.updateTracks(trackList)
                    noResultsView.isVisible = trackList.isEmpty()
                    recyclerView.isVisible = trackList.isNotEmpty()
                    noInternetView.isVisible = false
                } else {
                    noResultsView.isVisible = false
                    recyclerView.isVisible = false
                    noInternetView.isVisible = true
                    lastFailedQuery = searchText
                }
            }

            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                if (call.isCanceled) return
                noResultsView.isVisible = false
                recyclerView.isVisible = false
                noInternetView.isVisible = true
                lastFailedQuery = searchText
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

    companion object {
        private const val KEY_SEARCH_TEXT = "searchText"
    }

    private fun closeKeyboard(context: Context, searchEditText: EditText) {
        val closeKeyboard =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        closeKeyboard.hideSoftInputFromWindow(searchEditText.windowToken, 0)
    }

    private fun updateSearchHistory() {
        val history = searchHistory.getHistory()
        searchHistoryAdapter.updateTracks(history)
        searchHistoryContainer.visibility =
            if (history.isNotEmpty() && searchEditText.hasFocus()
                && searchEditText.text.isEmpty()
            ) View.VISIBLE else View.GONE
    }
}