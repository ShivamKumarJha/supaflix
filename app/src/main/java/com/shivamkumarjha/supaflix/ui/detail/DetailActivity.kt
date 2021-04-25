package com.shivamkumarjha.supaflix.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.shivamkumarjha.supaflix.R
import com.shivamkumarjha.supaflix.ui.player.PlayerActivity
import com.shivamkumarjha.supaflix.ui.search.SearchActivity
import com.shivamkumarjha.supaflix.ui.search.SearchType
import com.shivamkumarjha.supaflix.ui.theme.SupaflixTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : ComponentActivity() {
    private val hash by lazy {
        intent.getStringExtra(HASH)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (hash != null) {
            setContent {
                SupaflixTheme {
                    DetailScreen(hash!!, interactionEvents = {
                        handleInteractionEvents(it)
                    })
                }
            }
        }
    }

    private fun handleInteractionEvents(
        interactionEvents: DetailInteractionEvents
    ) {
        when (interactionEvents) {
            is DetailInteractionEvents.NavigateUp -> onBackPressed()
            is DetailInteractionEvents.OpenMovieDetail -> {
                startActivity(newIntent(this, interactionEvents.similarContents.hash))
                overridePendingTransition(0, 0)
            }
            is DetailInteractionEvents.OpenEpisode -> {
                startActivity(PlayerActivity.newIntent(this, interactionEvents.history))
                overridePendingTransition(0, 0)
            }
            is DetailInteractionEvents.ToggleFavourite -> {
                val message = if (interactionEvents.status)
                    interactionEvents.content.name + " " + resources.getString(R.string.favourite_add)
                else
                    interactionEvents.content.name + " " + resources.getString(R.string.favourite_remove)
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
            is DetailInteractionEvents.SearchGenre -> {
                startActivity(
                    SearchActivity.newIntent(
                        this,
                        interactionEvents.property,
                        SearchType.SEARCH_GENRE
                    )
                )
                overridePendingTransition(0, 0)
            }
            is DetailInteractionEvents.SearchActor -> {
                startActivity(
                    SearchActivity.newIntent(
                        this,
                        interactionEvents.property,
                        SearchType.SEARCH_ACTOR
                    )
                )
                overridePendingTransition(0, 0)
            }
            is DetailInteractionEvents.SearchDirector -> {
                startActivity(
                    SearchActivity.newIntent(
                        this,
                        interactionEvents.property,
                        SearchType.SEARCH_DIRECTOR
                    )
                )
                overridePendingTransition(0, 0)
            }
            is DetailInteractionEvents.SearchCountry -> {
                startActivity(
                    SearchActivity.newIntent(
                        this,
                        interactionEvents.property,
                        SearchType.SEARCH_COUNTRY
                    )
                )
                overridePendingTransition(0, 0)
            }
        }
    }

    companion object {
        const val HASH = "hash"
        fun newIntent(context: Context, hash: String) =
            Intent(context, DetailActivity::class.java).apply {
                putExtra(HASH, hash)
            }
    }
}