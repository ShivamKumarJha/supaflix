package com.shivamkumarjha.supaflix.ui.dashboard

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.core.content.FileProvider
import com.shivamkumarjha.supaflix.R
import com.shivamkumarjha.supaflix.config.Constants
import com.shivamkumarjha.supaflix.model.db.Download
import com.shivamkumarjha.supaflix.ui.detail.DetailActivity
import com.shivamkumarjha.supaflix.ui.player.PlayerActivity
import com.shivamkumarjha.supaflix.ui.search.SearchActivity
import com.shivamkumarjha.supaflix.ui.search.SearchType
import com.shivamkumarjha.supaflix.ui.theme.SupaflixTheme
import dagger.hilt.android.AndroidEntryPoint
import java.io.File


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
            is DashboardInteractionEvents.OpenDownloadedFile -> {
                playLocalVideo(interactionEvents.download)
            }
        }
    }

    private fun playLocalVideo(download: Download) {
        val filePath = download.filePath ?: return
        try {
            val uri = FileProvider.getUriForFile(
                this,
                this.applicationContext.packageName.toString() + ".provider",
                File(filePath)
            )
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(uri, "video/*")
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(intent)
        } catch (exception: ActivityNotFoundException) {
            Toast.makeText(
                this,
                getString(R.string.no_application_found_to_open_file),
                Toast.LENGTH_LONG
            ).show()
        } catch (exception: Exception) {
            Log.e(Constants.TAG, exception.localizedMessage ?: "")
        }
    }
}