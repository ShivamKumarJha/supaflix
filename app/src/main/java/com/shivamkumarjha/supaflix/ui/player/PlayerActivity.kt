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
import com.shivamkumarjha.supaflix.R
import com.shivamkumarjha.supaflix.model.db.Download
import com.shivamkumarjha.supaflix.model.db.History
import com.shivamkumarjha.supaflix.persistence.PreferenceManager
import com.shivamkumarjha.supaflix.receiver.DownloadFileReceiver
import com.shivamkumarjha.supaflix.ui.theme.SupaflixTheme
import com.shivamkumarjha.supaflix.utility.Utility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
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
                    PlayContent(history!!, interactionEvents = {
                        handleInteractionEvents(it)
                    })
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
            is PlayerInteractionEvents.OpenPlayer -> {
                if (preferenceManager.landscapePlayer) {
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                }
                GlobalScope.launch {
                    val subtitleUrl = when {
                        Utility.isURLReachable(interactionEvents.videoPlayerSource.subtitleUrl) -> interactionEvents.videoPlayerSource.subtitleUrl
                        !preferenceManager.showSubtitles -> null
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
            is PlayerInteractionEvents.DownloadVideo -> {
                if (interactionEvents.type.toLowerCase(Locale.ROOT).contains("dash") ||
                    interactionEvents.type.toLowerCase(Locale.ROOT).contains("m3u8") ||
                    interactionEvents.type.toLowerCase(Locale.ROOT).contains("hls")
                ) {
                    Toast.makeText(this, getString(R.string.download_failed), Toast.LENGTH_LONG)
                        .show()
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