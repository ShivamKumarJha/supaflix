package com.shivamkumarjha.supaflix.ui.dashboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import com.shivamkumarjha.supaflix.ui.detail.DetailActivity
import com.shivamkumarjha.supaflix.ui.player.PlayerActivity
import com.shivamkumarjha.supaflix.ui.search.SearchActivity
import com.shivamkumarjha.supaflix.ui.search.SearchType
import com.shivamkumarjha.supaflix.ui.theme.SupaflixTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@ExperimentalMaterialApi
class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SupaflixTheme {
                BottomNavigation(interactionEvents = {
                    handleInteractionEvents(it)
                })
            }
        }
    }

    private fun handleInteractionEvents(interactionEvents: DashboardInteractionEvents) {
        when (interactionEvents) {
            is DashboardInteractionEvents.OpenMovieDetail -> {
                startActivity(DetailActivity.newIntent(this, interactionEvents.hash))
                overridePendingTransition(0, 0)
            }
            is DashboardInteractionEvents.SearchMovie -> {
                startActivity(
                    SearchActivity.newIntent(
                        this,
                        interactionEvents.keyWord,
                        SearchType.SEARCH_MOVIE
                    )
                )
                overridePendingTransition(0, 0)
            }
            is DashboardInteractionEvents.SearchYear -> {
                startActivity(
                    SearchActivity.newIntent(
                        this,
                        interactionEvents.year,
                        SearchType.SEARCH_YEAR
                    )
                )
                overridePendingTransition(0, 0)
            }
            is DashboardInteractionEvents.SearchGenre -> {
                startActivity(
                    SearchActivity.newIntent(
                        this,
                        interactionEvents.property,
                        SearchType.SEARCH_GENRE
                    )
                )
                overridePendingTransition(0, 0)
            }
            is DashboardInteractionEvents.ResumePlayback -> {
                startActivity(PlayerActivity.newIntent(this, interactionEvents.history))
                overridePendingTransition(0, 0)
            }
            is DashboardInteractionEvents.ShortSearch -> {
                startActivity(SearchActivity.newIntent(this, interactionEvents.searchType))
                overridePendingTransition(0, 0)
            }
        }
    }
}