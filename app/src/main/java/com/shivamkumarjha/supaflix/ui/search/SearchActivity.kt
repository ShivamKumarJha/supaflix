package com.shivamkumarjha.supaflix.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.shivamkumarjha.supaflix.model.xmovies.Property
import com.shivamkumarjha.supaflix.ui.detail.DetailActivity
import com.shivamkumarjha.supaflix.ui.theme.SupaflixTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : ComponentActivity() {
    private val keyWord by lazy {
        intent.getStringExtra(KEYWORD)
    }
    private val property by lazy {
        intent.getParcelableExtra(PROPERTY) as Property?
    }
    private val searchType by lazy {
        intent.getSerializableExtra(SEARCH_MOVIE) as SearchType?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (searchType != null) {
            setContent {
                SupaflixTheme {
                    SearchMovieContent(searchType!!, property, keyWord, interactionEvents = {
                        handleInteractionEvents(it)
                    })
                }
            }
        }
    }

    private fun handleInteractionEvents(
        interactionEvents: SearchInteractionEvents
    ) {
        when (interactionEvents) {
            is SearchInteractionEvents.NavigateUp -> onBackPressed()
            is SearchInteractionEvents.OpenDetail -> {
                startActivity(DetailActivity.newIntent(this, interactionEvents.contents.hash))
                overridePendingTransition(0, 0)
            }
        }
    }

    companion object {
        const val KEYWORD = "search"
        const val PROPERTY = "property"
        const val SEARCH_MOVIE = "search_movie"

        fun newIntent(context: Context, searchType: SearchType) =
            Intent(context, SearchActivity::class.java).apply {
                putExtra(SEARCH_MOVIE, searchType)
            }

        fun newIntent(context: Context, keyWord: String, searchType: SearchType) =
            Intent(context, SearchActivity::class.java).apply {
                putExtra(KEYWORD, keyWord)
                putExtra(SEARCH_MOVIE, searchType)
            }

        fun newIntent(context: Context, property: Property, searchType: SearchType) =
            Intent(context, SearchActivity::class.java).apply {
                putExtra(PROPERTY, property)
                putExtra(SEARCH_MOVIE, searchType)
            }
    }
}