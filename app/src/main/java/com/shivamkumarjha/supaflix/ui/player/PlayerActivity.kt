package com.shivamkumarjha.supaflix.ui.player

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.lifecycle.lifecycleScope
import com.shivamkumarjha.supaflix.R
import com.shivamkumarjha.supaflix.model.db.Download
import com.shivamkumarjha.supaflix.model.db.History
import com.shivamkumarjha.supaflix.persistence.PreferenceManager
import com.shivamkumarjha.supaflix.receiver.DownloadFileReceiver
import com.shivamkumarjha.supaflix.ui.theme.SupaflixTheme
import com.shivamkumarjha.supaflix.utility.Utility
import com.shivamkumarjha.supaflix.utility.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PlayerActivity : ComponentActivity() {
    private var history: History? = null

    @Inject
    lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Keep screen on
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        history = intent.getParcelableExtra(HISTORY) as History?
        if (history != null) {
            setContent {
                SupaflixTheme {
                    Scaffold {
                        PlayContent(history!!, interactionEvents = {
                            handleInteractionEvents(it)
                        })
                    }
                }
            }
        }
    }

    private fun handleInteractionEvents(interactionEvents: PlayerInteractionEvents) {
        when (interactionEvents) {
            is PlayerInteractionEvents.NavigateUp -> {
                if (interactionEvents.isError) {
                    Toast.makeText(this, getString(R.string.server_error), Toast.LENGTH_LONG).show()
                }
                onBackPressed()
            }
            is PlayerInteractionEvents.ToggleOrientation -> {
                requestedOrientation =
                    if (interactionEvents.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    } else {
                        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    }
            }
            is PlayerInteractionEvents.OpenBrowser -> {
                Utility.openLinkInBrowser(interactionEvents.url, this)
                onBackPressed()
            }
            is PlayerInteractionEvents.OpenPlayer -> openPlayer(interactionEvents)
            is PlayerInteractionEvents.DownloadVideo -> {
                if (interactionEvents.type.equals("dash", ignoreCase = true) ||
                    interactionEvents.type.equals("m3u8", ignoreCase = true) ||
                    interactionEvents.type.equals("hls", ignoreCase = true) ||
                    interactionEvents.url.contains("m3u8", ignoreCase = true)
                ) {
                    this.toast(getString(R.string.download_not_supported))
                } else {
                    downloadFile(
                        interactionEvents.url,
                        interactionEvents.type,
                        interactionEvents.history
                    )
                }
            }
        }
    }

    private fun openPlayer(interactionEvents: PlayerInteractionEvents.OpenPlayer) {
        if (preferenceManager.landscapePlayer) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
        lifecycleScope.launch {
            val subtitleUrl = when {
                !preferenceManager.showSubtitles -> null
                interactionEvents.videoPlayerSource.subtitleUrl == null -> null
                Utility.isURLReachable(
                    lifecycleScope,
                    interactionEvents.videoPlayerSource.subtitleUrl
                ) -> interactionEvents.videoPlayerSource.subtitleUrl
                else -> null
            }
            interactionEvents.videoPlayerSource.subtitleUrl = subtitleUrl
            setContent {
                SupaflixTheme {
                    PlayerContent(interactionEvents.videoPlayerSource)
                }
            }
        }
    }

    private fun downloadFile(url: String, type: String, history: History) {
        val fileName = history.title.replace(" ", "") + "_" + history.episode + "." + type
        val description = history.description
        Toast.makeText(this, getString(R.string.downloading), Toast.LENGTH_LONG).show()
        val request = DownloadManager.Request(Uri.parse(url))
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setTitle(fileName)
            .setDescription(description)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(false)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadID = downloadManager.enqueue(request)

        //Register Download broadcast receiver
        val download = Download(downloadID, url, type, history)
        applicationContext.registerReceiver(
            DownloadFileReceiver(downloadManager, download),
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )
    }

    companion object {
        const val HISTORY = "history"
        fun newIntent(context: Context, history: History) =
            Intent(context, PlayerActivity::class.java).apply {
                putExtra(HISTORY, history)
            }
    }
}