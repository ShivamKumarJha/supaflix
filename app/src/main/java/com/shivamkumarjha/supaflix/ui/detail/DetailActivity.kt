package com.shivamkumarjha.supaflix.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import com.shivamkumarjha.supaflix.ui.player.PlayerActivity
import com.shivamkumarjha.supaflix.ui.search.SearchActivity
import com.shivamkumarjha.supaflix.ui.search.SearchType
import com.shivamkumarjha.supaflix.ui.theme.SupaflixTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@ExperimentalMaterialApi
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