package com.example.playlist_maker
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlist_maker.track.Track
import com.example.playlist_maker.track.TrackAdapter

class SearchActivity : AppCompatActivity() {
    private var searchText: String = ""
    private lateinit var searchEditText: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var trackAdapter: TrackAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        trackAdapter = TrackAdapter(Track.trackList)
        recyclerView.adapter = trackAdapter

        val buttonBackSettings = findViewById<Button>(R.id.arrow_back)
        buttonBackSettings.setOnClickListener{
            onBackPressed()
        }

        searchEditText = findViewById(R.id.search_edit_text)
        val searchClearButton =findViewById<ImageView>(R.id.clear_button)

        if (savedInstanceState != null) {
            searchText = savedInstanceState.getString(KEY_SEARCH_TEXT, "")
            searchEditText.setText(searchText)
        }

        searchClearButton.setOnClickListener{
            searchEditText.setText("")
            closeKeyboard(searchEditText.context, searchEditText)
            searchClearButton.visibility = View.GONE
        }


        searchEditText.addTextChangedListener(
            onTextChanged = { charSequence,_,_,_ ->
                searchClearButton.isVisible = !charSequence.isNullOrEmpty()
                searchText = charSequence.toString()
                filterTracks(charSequence.toString())
        })

    }

    private fun filterTracks(query: String) {
        val filteredList = Track.filterTracks(query)
        trackAdapter.updateTracks(filteredList)
    }



    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_SEARCH_TEXT,searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString(KEY_SEARCH_TEXT, "")
        searchEditText.setText(searchText)
    }

    companion object {
        private const val KEY_SEARCH_TEXT = "searchText"
    }


    fun closeKeyboard(context: Context, searchEditText: EditText){
        val closeKeyboard = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        closeKeyboard.hideSoftInputFromWindow(searchEditText.windowToken, 0)
    }



}

