package com.example.playlist_maker
import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity() {
    private var searchText: String = ""
    private lateinit var searchEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

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

        val simpleTextWatcher = object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchClearButton.visibility = clearButtonVisibility(p0)
                searchText = p0.toString()
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        }
        searchEditText.addTextChangedListener(simpleTextWatcher)

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

    fun clearButtonVisibility(p0: CharSequence?): Int{
        return if(p0.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    fun closeKeyboard(context: Context, searchEditText: EditText){
        val closeKeyboard = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        closeKeyboard.hideSoftInputFromWindow(searchEditText.windowToken, 0)
    }



}

