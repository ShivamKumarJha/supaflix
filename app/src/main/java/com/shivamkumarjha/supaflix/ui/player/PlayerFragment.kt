package com.shivamkumarjha.supaflix.ui.player

import android.app.DownloadManager
import android.content.Context
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.compose.material.Scaffold
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.shivamkumarjha.supaflix.R
import com.shivamkumarjha.supaflix.config.Constants
import com.shivamkumarjha.supaflix.model.db.Download
import com.shivamkumarjha.supaflix.model.db.History
import com.shivamkumarjha.supaflix.persistence.PreferenceManager
import com.shivamkumarjha.supaflix.receiver.DownloadFileReceiver
import com.shivamkumarjha.supaflix.ui.theme.SupaflixTheme
import com.shivamkumarjha.supaflix.utility.Utility
import com.shivamkumarjha.supaflix.utility.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PlayerFragment : Fragment() {
    //View
    private lateinit var composeView: ComposeView

    //Bundle args
    private val history by lazy {
        arguments?.getParcelable(Constants.HISTORY) as History?
    }

    @Inject
    lateinit var preferenceManager: PreferenceManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        composeView = ComposeView(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

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
            } else {
                findNavController().navigateUp()
            }
        }
        return composeView
    }

    override fun onDestroyView() {
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        super.onDestroyView()
    }

    private fun handleInteractionEvents(interactionEvents: PlayerInteractionEvents) {
        when (interactionEvents) {
            is PlayerInteractionEvents.NavigateUp -> {
                if (interactionEvents.isError) {
                    requireContext().toast(getString(R.string.server_error))
                }
                findNavController().navigateUp()
            }
            is PlayerInteractionEvents.ToggleOrientation -> {
                requireActivity().requestedOrientation =
                    if (interactionEvents.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    } else {
                        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    }
            }
            is PlayerInteractionEvents.OpenBrowser -> {
                Utility.openLinkInBrowser(interactionEvents.url, requireContext())
                findNavController().navigateUp()
            }
            is PlayerInteractionEvents.OpenPlayer -> {
                if (preferenceManager.landscapePlayer) {
                    requireActivity().requestedOrientation =
                        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                }
                GlobalScope.launch {
                    val subtitleUrl = when {
                        !preferenceManager.showSubtitles -> null
                        Utility.isURLReachable(interactionEvents.videoPlayerSource.subtitleUrl) -> interactionEvents.videoPlayerSource.subtitleUrl
                        else -> null
                    }
                    interactionEvents.videoPlayerSource.subtitleUrl = subtitleUrl
                    composeView.setContent {
                        SupaflixTheme {
                            PlayerContent(interactionEvents.videoPlayerSource)
                        }
                    }
                }
            }
            is PlayerInteractionEvents.DownloadVideo -> {
                if (interactionEvents.type.equals("dash", ignoreCase = true) ||
                    interactionEvents.type.equals("m3u8", ignoreCase = true) ||
                    interactionEvents.type.equals("hls", ignoreCase = true)
                ) {
                    requireContext().toast(getString(R.string.download_not_supported))
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
        requireContext().getString(R.string.downloading)
        val request = DownloadManager.Request(Uri.parse(url))
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setTitle(fileName)
            .setDescription(description)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(false)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        val downloadManager =
            requireActivity().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadID = downloadManager.enqueue(request)

        //Register Download broadcast receiver
        val download = Download(downloadID, url, type, history)
        requireActivity().applicationContext.registerReceiver(
            DownloadFileReceiver(downloadManager, download),
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )
    }
}